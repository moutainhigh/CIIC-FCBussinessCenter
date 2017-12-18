package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.ResultEntity;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.GroupTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.ItemTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.PrEntityIdClient;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
import com.github.pagehelper.PageInfo;
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

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/")
public class ItemController extends BaseController{

    @Autowired
    private PrItemService prItemService;

    @Autowired
    private PrEntityIdClient entityIdClient;

    private static final int GROUP_TEMPLATE = 0;
    private static final int GROUP = 1;

    @GetMapping(value = "/prItem")
    public JsonResult getPrItemList(@RequestParam String groupId,
                                      @RequestParam Integer parentType,
                                      @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(required = false, defaultValue = "50") Integer pageSize) {
        PageInfo<PrPayrollItemPO> pageInfo;
        if (GROUP_TEMPLATE == parentType) {
            // 获取薪资组模板的薪资项
            pageInfo = prItemService.getListByGroupTemplateId(groupId, pageNum, pageSize);
        } else if (GROUP == parentType) {
            // 获取薪资组的薪资项
            pageInfo = prItemService.getListByGroupId(groupId, pageNum, pageSize);
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

    @GetMapping(value = "/prItem/{id}")
    public JsonResult getPrItem(@PathVariable("id") String id) {
        PrPayrollItemPO result =  prItemService.getItemById(id);
        PrPayrollItemDTO resultItem = ItemTranslator.toPrPayrollItemDTO(result);
        return JsonResult.success(resultItem);
    }

    @PutMapping(value = "/prItem/{id}")
    public ResultEntity updatePrItem(@PathVariable("id") String entityId,
                                            @RequestBody PrItemEntity paramItem) {

        paramItem.setEntityId(entityId);
        Map<String, Object> resultMap  = prItemService.updateItem(paramItem);
        return ResultEntity.success(resultMap);
    }

    @GetMapping(value = "/prItemName")
    public ResultEntity getPrItemNameList(@RequestParam("managementId") String managementId) {

        List<String> resultList = prItemService.getNameList(managementId);
        return ResultEntity.success(resultList);
    }

    @GetMapping(value = "/prItemType")
    public ResultEntity getPrItemTypeList(@RequestParam("managementId") String managementId) {

        List<Integer> resultList = prItemService.getTypeList(managementId);
        return ResultEntity.success(resultList);
    }

    @PostMapping(value = "/prItem")
    public JsonResult newPrItem(@RequestBody PrPayrollItemDTO paramItem) {

        //TODO code、createedBy、modifiedBy的设置
        PrPayrollItemPO newParam = new PrPayrollItemPO();
        BeanUtils.copyProperties(paramItem, newParam);
        newParam.setItemCode(codeGenerator.genPrItemCode(paramItem.getManagementId()));
        if (newParam.getGroupTemplateId()!=null){
            List<PrPayrollItemPO> itemList = prItemService.getListByGroupTemplateId(
                    paramItem.getGroupTemplateId().toString(),0,0).getList();
            if (itemList != null) {
                if (itemList.stream()
                        .anyMatch(i -> i.getItemName().equals(newParam.getItemName()))) {
                    return JsonResult.faultMessage("薪资组模板中已存在同名薪资项");
                }
            }
        }
        if (newParam.getGroupId()!=null){
            List<PrPayrollItemPO> itemList = prItemService.getListByGroupId(
                    newParam.getGroupId().toString(),0,0).getList();
            if (itemList != null) {
                if (itemList.stream()
                        .anyMatch(i -> i.getItemName().equals(paramItem.getItemName()))) {
                    return JsonResult.faultMessage("薪资组中已存在同名薪资项");
                }
            }
        }
        //临时参数
        newParam.setCreatedBy("jiang");
        newParam.setModifiedBy("jiang");
        int resultId = prItemService.addItem(newParam);
        return resultId > 0 ? JsonResult.success(resultId) : JsonResult.faultMessage("新建薪资项失败");
    }

    @DeleteMapping("/prItem/{id}")
    public JsonResult deleteItem(@PathVariable("id") String id) {
        String[] ids = id.split(",");
        int i = prItemService.deleteItemByIds(Arrays.asList(ids));
        if (i >= 1){
            return JsonResult.success(i,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }

}
