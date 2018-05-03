package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagementcommandservice.api.PayrollGroupProxy;
import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollGroupDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollGroupHistoryDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.*;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.util.TranslatorUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.constants.MessageConst;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.DetailResponseDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.GetManagementRequestDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/10/24.
 * @author jiangtianning
 */
@RestController
@RequestMapping(value = "/api/salaryManagement")
public class GroupController implements PayrollGroupProxy{

    @Autowired
    private PrGroupService prGroupService;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private ManagementProxy managementProxy;

    @GetMapping(value = "/importPrGroup")
    public JsonResult importPrGroup(@RequestParam String from,
                                    @RequestParam String to) {
        boolean importResult = prGroupService.importPrGroup(from, to);
        if (!importResult) {
            throw new BusinessException("薪资组导入失败");
        }
        return JsonResult.success("导入成功");
    }

    /**
     * 拷贝薪资组
     * @param srcCode
     * @param newName
     * @return
     */
    @GetMapping(value = "/copyPrGroup")
    public JsonResult copyPrGroup(@RequestParam String srcCode,
                                  @RequestParam String newName,
                                  @RequestParam String managementId){
        PrPayrollGroupPO srcEntity = prGroupService.getItemByCode(srcCode);
        PrPayrollGroupPO newEntity = new PrPayrollGroupPO();
        BeanUtils.copyProperties(srcEntity, newEntity);
        newEntity.setGroupCode(codeGenerator.genPrGroupCode(newEntity.getManagementId()));
        newEntity.setGroupName(newName);
        newEntity.setManagementId(managementId);
        newEntity.setVersion("1.0");
        boolean result = prGroupService.copyPrGroup(srcEntity, newEntity);
        return result ? JsonResult.success(newEntity.getGroupCode(), MessageConst.PAYROLL_GROUP_COPY_SUCCESS)
                : JsonResult.faultMessage(MessageConst.PAYROLL_GROUP_COPY_FAIL);
    }



    /**
     * 查询薪资组列表
     * @param pageNum
     * @param pageSize
     * @param param
     * @return
     */
    @PostMapping(value = "/prGroupQuery")
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
     * 更新薪资组
     * @return
     */
    @PutMapping(value = "/prGroup/{code}")
    public JsonResult updatePrGroup(@PathVariable("code") String code, @RequestBody PrPayrollGroupDTO paramItem){

        PrPayrollGroupPO updateParam = new PrPayrollGroupPO();
        TranslatorUtils.copyNotNullProperties(paramItem, updateParam);
        updateParam.setGroupCode(code);
        updateParam.setModifiedBy(UserContext.getUserId());
        Integer result = prGroupService.updateItemByCode(updateParam);
        return JsonResult.success(result);
    }

    /**
     * 获取薪资组详情
     * @param code
     * @return
     */
    @GetMapping(value = "/prGroup/{code}")
    public JsonResult getPrGroup(@PathVariable("code") String code) {

        PrPayrollGroupPO result =  prGroupService.getItemByCode(code);
        PrPayrollGroupDTO prPayrollGroupDTO = GroupTranslator.toPrPayrollGroupDTO(result);
        GetManagementRequestDTO param = new GetManagementRequestDTO();
        String managementLabel;
        param.setQueryWords(prPayrollGroupDTO.getManagementId());
        com.ciicsh.gto.salecenter.apiservice.api.dto.core.JsonResult<List<DetailResponseDTO>> managementResult
                = managementProxy.getManagement(param);
        if (managementResult == null) {
            managementLabel = "";
        } else {
            List<DetailResponseDTO> managementList = managementResult.getObject();
            if (managementList == null || managementList.size() == 0) {
                managementLabel = "";
            } else {
                managementLabel = managementList.get(0).getTitle();
            }
        }
        prPayrollGroupDTO.setManagementLabel(managementLabel);
        return JsonResult.success(prPayrollGroupDTO);
    }

    /**
     * 新建薪资组
     * @param paramItem
     * @return
     */
    @PostMapping(value = "/prGroup")
    public JsonResult newPrGroup(@RequestBody PrPayrollGroupDTO paramItem){

        PrPayrollGroupPO newParam = new PrPayrollGroupPO();
        BeanUtils.copyProperties(paramItem, newParam);

        newParam.setCreatedBy(UserContext.getUserId());
        newParam.setModifiedBy(UserContext.getUserId());
        int result= prGroupService.addItem(newParam);
        return result > 0 ? JsonResult.success(newParam.getGroupCode()) : JsonResult.faultMessage("新建薪资组失败");
    }

    /**
     * 删除薪资组
     * @param code
     * @return
     */
    @DeleteMapping(value = "/prGroup/{code}")
    public JsonResult deleteItem(@PathVariable("code") String code){
        String[] codes = code.split(",");
        int i = prGroupService.deleteByCodes(Arrays.asList(codes));
        if (i >= 1){
            return JsonResult.success(i,"删除成功");
        }else {
            return JsonResult.faultMessage();
        }
    }

    @GetMapping("/getpayrollgroupnames")
    public  JsonResult getPayrollGroupNames(@RequestParam String managementId){
        List<KeyValuePO> keyValues = prGroupService.getPayrollGroupNames(managementId);
        return JsonResult.success(keyValues);
    }

    @GetMapping("/prGroupName")
    public JsonResult<List<HashMap<String, String>>> getPayrollGroupNameList(@RequestParam("query") String query,
                                                                             @RequestParam(value = "managementId", required = false, defaultValue = "") String managementId){
        List<HashMap<String, String>> nameList = prGroupService.getPrGroupNameList(query, managementId);
        return JsonResult.success(nameList);
    }

    @PutMapping("/approvePayrollGroup/{code}")
    public JsonResult approvePayrollGroup(@PathVariable("code") String code, @RequestBody PrPayrollGroupDTO paramItem){
        PrPayrollGroupPO updateParam = new PrPayrollGroupPO();
        TranslatorUtils.copyNotNullProperties(paramItem, updateParam);
        updateParam.setGroupCode(code);
        updateParam.setModifiedBy("system");
        boolean result = prGroupService.approvePrGroup(updateParam);
        return result ? JsonResult.success(null, MessageConst.PAYROLL_GROUP_APPROVE_SUCCESS)
                : JsonResult.faultMessage(MessageConst.PAYROLL_GROUP_APPROVE_FAIL);
    }

    @GetMapping("/lastVersionPayrollGroupItems")
    public JsonResult getLastVersion(@RequestParam("code") String code) {
//        JSONObject result = new JSONObject();
        PrPayrollGroupHistoryPO lastVersionData = prGroupService.getLastVersion(code);
        if (lastVersionData == null) {
            return JsonResult.faultMessage("该薪资组没有历史版本");
        }
        List<PrPayrollItemPO> lastVersionItems = JSON.parseObject(lastVersionData.getPayrollGroupHistory(),
                new TypeReference<List<PrPayrollItemPO>>() {});
        List<PrPayrollItemDTO> lastVersionItemsDTO = lastVersionItems
                .stream()
                .map(ItemTranslator::toPrPayrollItemDTO)
                .collect(Collectors.toList());
        PrPayrollGroupHistoryDTO resultObject = new PrPayrollGroupHistoryDTO();
        BeanUtils.copyProperties(lastVersionData, resultObject);
        resultObject.setItemDTOList(lastVersionItemsDTO);
        return JsonResult.success(resultObject);
    }

    @GetMapping("/queryGroupKeyValues")
    public  JsonResult queryGroupKeyValues(@RequestParam String managementId,@RequestParam String query){
        List<KeyValuePO> keyValues = prGroupService.getPayrollGroupNames(managementId);
        List<KeyValuePO> result = new ArrayList<>();
        result.clear();
        if(CommonUtils.isContainChinese(query)){
            keyValues.forEach(model ->{
                if(model.getValue().contains(query)){
                    result.add(model);
                }
            });
        }else {
            keyValues.forEach(model ->{
                if(model.getKey().contains(query)){
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
