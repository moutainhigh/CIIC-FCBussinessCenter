package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.ResultEntity;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollAccountSetDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupTemplateDTO;
import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.ciicsh.gto.salarymanagementcommandservice.controller.NormalBatchController.MgrData;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeGroupTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.GroupTemplateTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.GroupTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.PrEntityIdClient;
import com.ciicsh.gto.salarymanagementcommandservice.exception.BusinessException;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.util.TranslatorUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/10/24.
 * @@author jiangtianning
 */
@RestController
@RequestMapping(value = "/")
public class GroupController {

    @Autowired
    private PrGroupService prGroupService;

//    @Autowired
//    private PrEntityIdClient entityIdClient;

    @Autowired
    private CodeGenerator codeGenerator;

    @GetMapping(value = "/prCode")
    public String getCode() {
        return codeGenerator.genPrGroupCode("001");
    }

    @GetMapping(value = "/prGroup/{id}/import")
    public ResultEntity importPrGroup(@RequestParam String from,
                                      @PathVariable("id") String to) {
        boolean importResult = prGroupService.importPrGroup(from, to);
        if (!importResult) {
            throw new BusinessException("薪资组导入失败");
        }
        return ResultEntity.success("");
    }


    @PostMapping(value = "/prGroupQuery")
    @ResponseBody
    public JsonResult getPrGroupList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "50")  Integer pageSize,
                                     @RequestBody PrPayrollGroupDTO param){

        PrPayrollGroupPO paramEntity = new PrPayrollGroupPO();
        BeanUtils.copyProperties(param, paramEntity);
        PageInfo<PrPayrollGroupPO> pageInfo =  prGroupService.getList(paramEntity, pageNum, pageSize);
        List<PrPayrollGroupDTO> resultList = pageInfo.getList()
                .stream()
                .map(GroupTranslator::toPrPayrollGroupDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollGroupDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }

    /**
     * 获取薪资组名列表
     * @param managementId
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/prGroupName")
    @ResponseBody
    public ResultEntity getPrGroupNameList(@RequestParam String managementId,
                                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum){
        PrGroupEntity paramItem = new PrGroupEntity();
        setCommonParam(paramItem, managementId, null);
        List<String> resultList = prGroupService.getNameList(managementId);
        return ResultEntity.success(resultList);
    }


    /**
     * 更新薪资组
     * @return
     */
    @PutMapping(value = "/prGroup/{id}")
    @ResponseBody
    public JsonResult updatePrGroup(@PathVariable("id") String id, @RequestBody PrPayrollGroupDTO paramItem){

        PrPayrollGroupPO updateParam = new PrPayrollGroupPO();
        TranslatorUtils.copyNotNullProperties(paramItem, updateParam);
        updateParam.setId(Integer.parseInt(id));
        updateParam.setModifiedBy("jiang");
        Integer result = prGroupService.updateItemById(updateParam);
        return JsonResult.success(result);
    }


    /**
     * 获取薪资组详情
     * @param id
     * @return
     */
    @GetMapping(value = "/prGroup/{id}")
    @ResponseBody
    public JsonResult getPrGroup(@PathVariable("id") String id) {

        PrPayrollGroupPO result =  prGroupService.getItemById(id);
        return JsonResult.success(result);
    }

    /**
     * 新建薪资组
     * @param paramItem
     * @return
     */
    @PostMapping(value = "/prGroup")
    @ResponseBody
    public JsonResult newPrGroup(@RequestBody PrPayrollGroupDTO paramItem){

        PrPayrollGroupPO newParam = new PrPayrollGroupPO();
        BeanUtils.copyProperties(paramItem, newParam);
        newParam.setGroupCode(codeGenerator.genPrGroupCode(newParam.getManagementId()));
        newParam.setCreatedBy("jiang");
        newParam.setModifiedBy("jiang");
        // Version 生成
        newParam.setVersion("1.0");
        int result= prGroupService.addItem(newParam);
        return result > 0 ? JsonResult.success(result) : JsonResult.faultMessage("新建薪资组失败");
    }

    @DeleteMapping(value = "/prGroup/{prGroupId}/prItem/{prItemId}")
    public ResultEntity deletePrItemFromPrGroup(@PathVariable("prGroupId") String prGroupId,
                                                       @PathVariable("prItemId") String prItemId,
                                                       @RequestParam String prItemName) {
        Map<String, Object> result =  prGroupService.deletePrItemFromGroup(prGroupId, prItemId, prItemName);
        return ResultEntity.success(result);
    }

    @DeleteMapping(value = "/prGroup/{id}")
    public JsonResult deleteItem(@PathVariable("id") String id){
        String[] ids = id.split(",");
        int i = prGroupService.deleteByIds(Arrays.asList(ids));
        if (i >= 1){
            return JsonResult.success(i,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }


    @GetMapping("/getpayrollgroupnames")
    @ResponseBody
    public  JsonResult getPayrollGroupNames(@RequestParam String managementId){
        List<KeyValuePO> keyValues = prGroupService.getPayrollGroupNames(managementId);
        return JsonResult.success(keyValues);
    }

    @GetMapping("/queryGroupKeyValues")
    @ResponseBody
    public  JsonResult queryGroupKeyValues(@RequestParam String managementId,@RequestParam String query){
        List<KeyValuePO> keyValues = prGroupService.getPayrollGroupNames(managementId);
        List<KeyValuePO> result = new ArrayList<>();
        result.clear();
        if(CommonUtils.isContainChinese(query)){
            keyValues.forEach(model ->{
                if(model.getValue().indexOf(query) >-1){
                    result.add(model);
                }
            });
        }else {
            keyValues.forEach(model ->{
                if(model.getKey().indexOf(query) >-1){
                    result.add(model);
                }
            });
        }
        return JsonResult.success(result);
    }

    private void setCommonParam(PrGroupEntity paramModel, String managementId, String name) {
        if (paramModel != null) {
            paramModel.setManagementId(managementId);
        }
        if (name != null) {
            paramModel.setName(name);
        }
    }
}
