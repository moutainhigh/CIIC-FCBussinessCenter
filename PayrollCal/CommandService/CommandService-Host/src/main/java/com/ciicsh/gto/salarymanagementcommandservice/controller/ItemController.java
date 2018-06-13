package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DecimalProcessTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DefaultValueStyleEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.utils.EnumHelpUtil;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.ItemTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.constants.MessageConst;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/api/salaryManagement")
public class ItemController extends BaseController{

    @Autowired
    private PrItemService itemService;

    private static final int GROUP_TEMPLATE = 0;
    private static final int GROUP = 1;

    /**
     * 获取薪资项列表
     * @param groupCode
     * @param parentType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/prItem")
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
     * 获取薪资项
     * @param code
     * @return
     */
    @GetMapping(value = "/prItem/{code}")
    public JsonResult getPrItem(@PathVariable("code") String code) {
        PrPayrollItemPO result =  itemService.getItemByCode(code);
        PrPayrollItemDTO resultItem = ItemTranslator.toPrPayrollItemDTO(result);
        return JsonResult.success(resultItem);
    }

    /**
     * 更新薪资项
     * @param code
     * @param paramItem
     * @return
     */
    @PutMapping(value = "/prItem/{code}")
    public JsonResult updatePrItem(@PathVariable("code") String code,
                                            @RequestBody PrPayrollItemPO paramItem) {
        paramItem.setItemCode(code);
        try {
            int result = itemService.updateItem(paramItem);
            return JsonResult.success(result);
        } catch (BusinessException be) {
            return JsonResult.faultMessage(be.getMessage());
        } catch (Exception e) {
            return JsonResult.faultMessage(MessageConst.PAYROLL_ITEM_UPDATE_FAIL);
        }
    }

    /**
     * 获取薪资项类型列表
     * @return
     */
    @GetMapping(value = "/prItemType")
    public JsonResult getPrItemTypeList() {
        return JsonResult.success(EnumHelpUtil.getLabelValueList(ItemTypeEnum.class));
    }

    /**
     * 新建薪资项
     * @param paramItem
     * @return
     */
    @PostMapping(value = "/prItem")
    public JsonResult newPrItem(@RequestBody PrPayrollItemDTO paramItem) {

        //TODO code、createedBy、modifiedBy的设置
        PrPayrollItemPO newParam = new PrPayrollItemPO();
        BeanUtils.copyProperties(paramItem, newParam);
//        newParam.setItemCode(codeGenerator.genPrItemCode(paramItem.getManagementId()));
        if (!StringUtils.isEmpty(newParam.getPayrollGroupTemplateCode())){
            List<PrPayrollItemPO> itemList = itemService.getListByGroupTemplateCode(
                    paramItem.getPayrollGroupTemplateCode(),0,0).getList();
            if (itemList != null) {
                if (itemList.stream()
                        .anyMatch(i -> i.getItemName().equals(newParam.getItemName()))) {
                    return JsonResult.faultMessage("薪资组模板中已存在同名薪资项");
                }
            }
        }
        if (!StringUtils.isEmpty(newParam.getPayrollGroupCode())){
            List<PrPayrollItemPO> itemList = itemService.getListByGroupCode(
                    newParam.getPayrollGroupCode(),0,0).getList();
            if (itemList != null) {
                if (itemList.stream()
                        .anyMatch(i -> i.getItemName().equals(paramItem.getItemName()))) {
                    return JsonResult.faultMessage("薪资组中已存在同名薪资项");
                }
            }
        }
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

    @DeleteMapping("/prItem/{code}")
    public JsonResult deleteItem(@PathVariable("code") String code) {
        String[] codes = code.split(",");
        int i = itemService.deleteItemByCodes(Arrays.asList(codes));
        if (i >= 1){
            return JsonResult.success(i,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }

    @PostMapping("/prItemDisplayPriority")
    public JsonResult updateDisplayPriority(@RequestBody List<String> codes) {
        return itemService.updateDisplayPriority(codes) ?
                JsonResult.message(true,"更新薪资项显示顺序成功") : JsonResult.faultMessage("更新薪资项显示顺序失败");
    }

    @PostMapping("/prItemCalPriority")
    public JsonResult updateCalPriority(@RequestBody List<String> codes) {
        return itemService.updateCalPriority(codes) ?
                JsonResult.message(true,"更新薪资项计算顺序成功") : JsonResult.faultMessage("更新薪资项计算顺序失败");
    }
//    @RequestMapping("/addTest")
//    public String addTestData() throws Exception{
//        List<PrPayrollItemPO> items = this.getItems();
//        for(Map.Entry<String,String> model:getPayrollGroup().entrySet()){
//            items.forEach(x->{
//                x.setManagementId(model.getKey());
//                x.setItemCode(codeGenerator.genPrItemCode(model.getKey()));
//                x.setPayrollGroupCode(model.getValue());
//                x.setActive(true);
//                x.setCreatedBy("macor");
//                x.setCreatedTime(new Date());
//                x.setModifiedBy("macor");
//                x.setModifiedTime(new Date());
//                itemService.addItem(x);
//            });
//        }
//        return "添加成功!";
//    }

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
}
