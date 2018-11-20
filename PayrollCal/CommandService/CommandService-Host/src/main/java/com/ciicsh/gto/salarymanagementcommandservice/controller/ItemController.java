package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DecimalProcessTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DefaultValueStyleEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.utils.EnumHelpUtil;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.ItemTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.constants.MessageConst;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/api/salaryManagement")
public class ItemController extends BaseController {

    @Autowired
    private PrItemService itemService;

    private static final int GROUP_TEMPLATE = 0;
    private static final int GROUP = 1;

    private final static Pattern DATE_REGEX_PATTERN = Pattern.compile("\\$\\d{4}(-)\\d{1,2}(-)\\d{1,2}\\$");
    private final static Pattern XZX_REGEX_PATTERN = Pattern.compile("\\[([\\u4E00-\\u9FA5]+|[a-zA-Z0-9]+)\\]");

    /**
     * 获取薪资项列表
     *
     * @param groupCode
     * @param parentType
     * @param pageNum
     * @param pageSize
     * @return
     *
     */
    @GetMapping(value = "/getPrItemPage")
    public JsonResult getPrItemList(@RequestParam String groupCode,
                                    @RequestParam Integer parentType,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                    @RequestParam(required = false, defaultValue = "50") Integer pageSize) {
        PageInfo<PrPayrollItemPO> pageInfo;
        if (GROUP_TEMPLATE == parentType) {
            // 获取薪资组模板的薪资项
            pageInfo = itemService.getListByGroupTemplateCode(groupCode, pageNum, pageSize, true);
        } else if (GROUP == parentType) {
            // 获取薪资组的薪资项
            pageInfo = itemService.getListByGroupCode(groupCode, pageNum, pageSize, true);
        } else {
            return JsonResult.faultMessage("非法的参数parentType: " + parentType);
        }
        List<PrPayrollItemDTO> resultList = pageInfo.getList()
                .stream()
                .map(ItemTranslator::toPrPayrollItemDTO)
                .collect(Collectors.toList());

        PageInfo<PrPayrollItemDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }

    /**
     * 根据薪资项ID查询信息项明细
     *
     * @param id 薪资项ID
     * @return 薪资项明细
     */
    @GetMapping(value = "/getPrItemDetailById/{id}")
    public JsonResult getPrItemDetailById(@PathVariable("id") Integer id) {
        PrPayrollItemPO itemPO = itemService.selectById(id);
        PrPayrollItemDTO resultItem = ItemTranslator.toPrPayrollItemDTO(itemPO);
        return JsonResult.success(resultItem);
    }

    /**
     * 根据薪资项ID更新薪资项
     *
     * @param paramDTO 更新明细
     * @return 更新结果
     */
    @PutMapping(value = "/updatePrItemById")
    public JsonResult updatePrItemById(@RequestBody PrPayrollItemDTO paramDTO) {
        // 校验同名薪资项
        JsonResult checkJsonResult = checkSameNamePrItem(paramDTO);
        if (checkJsonResult != null) {
            return checkJsonResult;
        }

        try {
            PrPayrollItemPO paramPO = new PrPayrollItemPO();
            BeanUtils.copyProperties(paramDTO, paramPO);

            if (StringUtils.isNotBlank(paramDTO.getOriginCondition())) {
                paramPO.setOriginCondition(processDateType(paramDTO.getOriginCondition()));
            }
            if (StringUtils.isNotBlank(paramDTO.getItemCondition())) {
                paramPO.setItemCondition(processDateType(paramDTO.getItemCondition()));
            }
            if (StringUtils.isNotBlank(paramDTO.getFullFormula())) {
                paramPO.setFullFormula(processDateType(paramDTO.getFullFormula()));
            }

            int result = itemService.updatePrItemById(paramPO);
            return JsonResult.success(result);
        } catch (BusinessException be) {
            return JsonResult.faultMessage(be.getMessage());
        } catch (Exception e) {
            return JsonResult.faultMessage(MessageConst.PAYROLL_ITEM_UPDATE_FAIL);
        }
    }

    /**
     * 获取薪资项类型列表
     *
     * @return
     */
    @GetMapping(value = "/prItemType")
    public JsonResult getPrItemTypeList() {
        return JsonResult.success(EnumHelpUtil.getLabelValueList(ItemTypeEnum.class));
    }

    /**
     * 新建薪资项
     *
     * @param paramItem
     * @return
     */
    @PostMapping(value = "/prItem")
    public JsonResult newPrItem(@RequestBody PrPayrollItemDTO paramItem) {

        //TODO code、createedBy、modifiedBy的设置
        PrPayrollItemPO newParam = new PrPayrollItemPO();
        BeanUtils.copyProperties(paramItem, newParam);
//        newParam.setItemCode(codeGenerator.genPrItemCode(paramItem.getManagementId()));
        // 校验同名薪资项
        JsonResult checkJsonResult = checkSameNamePrItem(paramItem);
        if (checkJsonResult != null) return checkJsonResult;
        //临时参数
        newParam.setCreatedBy(UserContext.getUserId());
        newParam.setModifiedBy(UserContext.getUserId());
        int resultId = 0;
        try {
            resultId = itemService.addItem(newParam);
        } catch (BusinessException be) {
            return JsonResult.faultMessage(be.getMessage());
        }
        return resultId > 0 ? JsonResult.success(newParam.getItemCode()) : JsonResult.faultMessage("新建薪资项失败");
    }

    /**
     * 根据薪资项ID删除薪资项
     *
     * @param id 薪资项ID
     * @return 删除条数
     */
    @DeleteMapping("/deletePrItemById/{id}")
    public JsonResult deletePrItemById(@PathVariable("id") Integer id) {
        int i = itemService.deletePrItemById(id);
        if (i >= 1) {
            return JsonResult.success(i, "删除成功");
        } else {
            return JsonResult.faultMessage();
        }
    }

    @PostMapping("/prItemDisplayPriority")
    public JsonResult updateDisplayPriority(@RequestBody List<Integer> ids) {
        return itemService.updateDisplayPriority(ids) ?
                JsonResult.message(true, "更新薪资项显示顺序成功") : JsonResult.faultMessage("更新薪资项显示顺序失败");
    }

    @PostMapping("/prItemCalPriority")
    public JsonResult updateCalPriority(@RequestBody List<Integer> ids) {
        return itemService.updateCalPriority(ids) ?
                JsonResult.message(true, "更新薪资项计算顺序成功") : JsonResult.faultMessage("更新薪资项计算顺序失败");
    }

    @GetMapping("/decimalProcessType")
    public JsonResult getDecimalProcessTypeEnums() {
        return JsonResult.success(EnumHelpUtil.getLabelValueList(DecimalProcessTypeEnum.class));
    }

    @GetMapping("/defaultValueStyle")
    public JsonResult getDefaultValueStyleEnums() {
        return JsonResult.success(EnumHelpUtil.getLabelValueList(DefaultValueStyleEnum.class));
    }

    @GetMapping("/dataType")
    public JsonResult getDataTypeEnums() {
        return JsonResult.success(EnumHelpUtil.getLabelValueList(DataTypeEnum.class));
    }

    /**
     * 新增和编辑时校验薪资项是否有重名
     *
     * @param paramItem
     * @return
     */
    public JsonResult checkSameNamePrItem(PrPayrollItemDTO paramItem) {
        // 获取当前编辑的薪资项name和code
        String editPrItemCode = paramItem.getItemCode();
        if (!org.apache.commons.lang.StringUtils.isEmpty(paramItem.getPayrollGroupTemplateCode())) {
            List<PrPayrollItemPO> itemList = itemService.getListByGroupTemplateCode(paramItem.getPayrollGroupTemplateCode(), true);
            if (itemList != null) {
                itemList = checkItemList(editPrItemCode, itemList);
                if (itemList.stream()
                        .anyMatch(i -> i.getItemName().equals(paramItem.getItemName()))) {
                    return JsonResult.faultMessage("薪资组模板中已存在同名薪资项");
                }
            }
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(paramItem.getPayrollGroupCode())) {
            List<PrPayrollItemPO> itemList = itemService.getListByGroupCode(paramItem.getPayrollGroupCode(), true);
            if (itemList != null) {
                itemList = checkItemList(editPrItemCode, itemList);
                if (itemList.stream()
                        .anyMatch(i -> i.getItemName().equals(paramItem.getItemName()))) {
                    return JsonResult.faultMessage("薪资组中已存在同名薪资项");
                }
            }
        }
        return null;
    }

    /**
     * 处理条件中日期类型
     * @param input
     * @return input
     */
    private String processDateType(String input) {
        Matcher dateMatcher = null;
        if (input != null) {
            dateMatcher = DATE_REGEX_PATTERN.matcher(input);
        }

        while (dateMatcher.find()) {
            Matcher xzxMatcher = XZX_REGEX_PATTERN.matcher(input);
            String ori = input.substring(dateMatcher.start(), dateMatcher.end());
            String update = "new Date('" + ori.replaceAll("\\$","") + "')";
            input = input.replace(ori, update);

            //todo: 假设条件有日期型值的话，条件里所有薪资项都是日期型，以后可能要优化。11/12/2018
            while (xzxMatcher.find()) {
                ori = input.substring(xzxMatcher.start(), xzxMatcher.end());
                update = "new Date(" + ori + ")";
                input = input.replace(ori, update);
            }
        }
        return input;
    }

    /**
     * editPrItemCode不为空，则为编辑，编辑时忽略当前编辑项与数据库中自身的薪资项name同名校验
     * editPrItemCode为空，则为新增，无需做checkItemList的步骤
     *
     * @param editPrItemCode 当前编辑的薪资项code
     * @param itemList       数据库中查出来的薪资项列表
     * @return 参与校验的薪资项列表
     */
    private List<PrPayrollItemPO> checkItemList(String editPrItemCode, List<PrPayrollItemPO> itemList) {
        if (StringUtils.isNotBlank(editPrItemCode)) {
            itemList.removeIf(item -> item.getItemCode().equals(editPrItemCode));
        }
        return itemList;
    }
}
