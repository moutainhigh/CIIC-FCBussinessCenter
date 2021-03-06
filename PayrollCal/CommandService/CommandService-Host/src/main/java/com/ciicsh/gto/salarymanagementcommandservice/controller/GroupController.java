package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.EmpExtendFieldTemplateProxy;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.EmpExtendFieldTemplateListDTO;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.EmployeeExtendFieldDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.api.PayrollGroupProxy;
import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollGroupDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollGroupHistoryDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.*;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.util.TranslatorUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.constants.MessageConst;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.DetailResponseDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.GetManagementRequestDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
    private ManagementProxy managementProxy;

    @Autowired
    private EmpExtendFieldTemplateProxy empExtendFieldTemplateProxy;

    @GetMapping(value = "/importPrGroup")
    public JsonResult importPrGroup(@RequestParam String from,
                                    @RequestParam String to,
                                    @RequestParam(defaultValue = "false") Boolean fromTemplate) {
        boolean importResult;
        try {
            importResult = prGroupService.importPrGroup(from, to, fromTemplate);
        } catch (BusinessException be) {
            return JsonResult.faultMessage(be.getMessage());
        }
        if (!importResult) {
            throw new BusinessException("薪资组导入失败");
        }
        return JsonResult.success("导入成功");
    }

    /**
     * 复制薪资组
     * @param srcCode
     * @param newName
     * @return
     */
    @GetMapping(value = "/copyPrGroup")
    public JsonResult copyPrGroup(@RequestParam String srcCode,
                                  @RequestParam String newName,
                                  @RequestParam String managementId,
                                  @RequestParam String remark,
                                  @RequestParam boolean isCopyRemark,
                                  @RequestParam(required = false) Long empExtendFieldTemplateId){
        PrPayrollGroupPO srcEntity = prGroupService.getItemByCode(srcCode);
        PrPayrollGroupPO newEntity = new PrPayrollGroupPO();
        BeanUtils.copyProperties(srcEntity, newEntity);
        newEntity.setEmpExtendFieldTemplateId(empExtendFieldTemplateId);
        newEntity.setGroupName(newName);
        newEntity.setManagementId(managementId);
        newEntity.setRemark(isCopyRemark ? srcEntity.getRemark() : remark);
        // 标记此薪资组是复制而来
        newEntity.setOperateType(OperateTypeEnum.COPY.getValue());
        boolean result;
        try {
            result = prGroupService.copyPrGroup(srcEntity, newEntity);
        } catch (BusinessException be) {
            return JsonResult.faultMessage(be.getMessage());
        }
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

        // 赋值管理方名称
        int size = resultList.size();
        for (int i = 0; i < size; i++) {
            PrPayrollGroupDTO prPayrollGroupDTO = resultList.get(i);
            prPayrollGroupDTO.setManagementLabel(getManagementName(prPayrollGroupDTO.getManagementId()));
        }

        PageInfo<PrPayrollGroupDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }

    /**
     * 根据管理方ID获取管理方名称
     *
     * @param managementId
     * @return
     */
    public String getManagementName(String managementId){
        if (org.springframework.util.StringUtils.isEmpty(managementId)) {
            return "";
        }

        String managementLabel;

        GetManagementRequestDTO param = new GetManagementRequestDTO();
        param.setQueryWords(managementId);
        com.ciicsh.gto.salecenter.apiservice.api.dto.core.JsonResult<List<DetailResponseDTO>> managementResult = managementProxy.getManagement(param);
        if (ObjectUtils.isEmpty(managementResult)) {
            managementLabel = "";
        } else {
            List<DetailResponseDTO> managementList = managementResult.getObject();
            if (CollectionUtils.isEmpty(managementList)) {
                managementLabel = "";
            } else {
                managementLabel = managementList.get(0).getTitle();
            }
        }

        return managementLabel;
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
        Integer result;
        try {
            result = prGroupService.updateItemByCode(updateParam);
        } catch (BusinessException be) {
            return JsonResult.faultMessage(be.getMessage());
        }
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
        int result;
        try {
            result = prGroupService.addItem(newParam);
        } catch (BusinessException be) {
            return JsonResult.faultMessage(be.getMessage());
        }
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
    public JsonResult<List<HashMap<String, String>>> getPayrollGroupNameList(
            @RequestParam(value = "query", required = false, defaultValue = "") String name,
            @RequestParam(value = "managementId", required = false, defaultValue = "") String managementId){
        List<HashMap<String, String>> nameList = prGroupService.getPrGroupNameList(name, managementId);
        return JsonResult.success(nameList);
    }

    /**
     * 审批薪资组
     * @param paramItem 薪资组信息
     * @return 审批结果
     */
    @PutMapping("/approvePayrollGroup")
    public JsonResult approvePayrollGroup(@RequestBody PrPayrollGroupDTO paramItem) {
        PrPayrollGroupPO updateParam = new PrPayrollGroupPO();
        TranslatorUtils.copyNotNullProperties(paramItem, updateParam);
        updateParam.setModifiedBy(UserContext.getUserId());
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

    /**
     * 查询管理方下雇员扩展字段模板列表
     *
     * @return
     */
    @GetMapping("/queryExtendFieldTemplateList")
    public JsonResult queryExtendFieldTemplateList(@RequestParam String managementId) {
        com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.JsonResult<List<EmpExtendFieldTemplateListDTO>> listJsonResult = empExtendFieldTemplateProxy.listTemplates(managementId);
        if (!ObjectUtils.isEmpty(listJsonResult)) {
            List<EmpExtendFieldTemplateListDTO> templateListDTOList = listJsonResult.getData();
            if (!CollectionUtils.isEmpty(templateListDTOList)) {
                return JsonResult.success(templateListDTOList);
            } else {
                return JsonResult.success(new ArrayList<>());
            }
        }

        return JsonResult.faultMessage("查询模板列表异常，请稍后重试！");
    }

    /**
     * 查询模板下雇员扩展字段列表
     *
     * @return
     */
    @GetMapping("/queryExtendFieldList")
    public JsonResult queryExtendFieldList(@RequestParam Long empExtendFieldTemplateId) {
        com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.JsonResult<List<EmployeeExtendFieldDTO>> listJsonResult = empExtendFieldTemplateProxy.listTemplateFields(empExtendFieldTemplateId);
        if (!ObjectUtils.isEmpty(listJsonResult)) {
            List<EmployeeExtendFieldDTO> extendFieldDTOList = listJsonResult.getData();
            if (!CollectionUtils.isEmpty(extendFieldDTOList)) {
                return JsonResult.success(extendFieldDTOList);
            } else {
                return JsonResult.success(new ArrayList<>());
            }
        }

        return JsonResult.faultMessage("查询扩展字段列表异常，请稍后重试！");
    }
}
