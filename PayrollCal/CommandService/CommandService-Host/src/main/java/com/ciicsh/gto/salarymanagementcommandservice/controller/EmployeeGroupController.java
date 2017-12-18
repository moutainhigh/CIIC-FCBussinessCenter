package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.EmployeeGroupProxy;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.ResultEntity;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.salarymanagement.entity.PrEmployeeGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeGroupTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/10/25.
 */
@RestController
public class EmployeeGroupController extends BaseController implements EmployeeGroupProxy{

    @Autowired
    private EmployeeGroupService employeeGroupService;

    @Override
    public JsonResult getEmployeeGroupNames(@RequestParam String managementId,@RequestParam String query) {
        List<KeyValuePO> keyValues = employeeGroupService.getEmployeeGroupNames(managementId);
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

    @Override
    public JsonResult getEmployeeGroups(@RequestBody PrEmpGroupDTO prEmpGroupDTO,
                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {
        PrEmpGroupPO empGroupPO  = EmployeeGroupTranslator.toPrEmpGroupPO(prEmpGroupDTO);
        PageInfo<PrEmpGroupPO> pageInfo =  employeeGroupService.getEmployeeGroups(empGroupPO, pageNum,pageSize);
        List<PrEmpGroupDTO> empGroups = pageInfo.getList()
                .stream()
                .map(EmployeeGroupTranslator::toPrEmpGroupDTO)
//                .map((dto)->{
//                    if(getManagement().containsKey(dto.getManagementId())){
//                        dto.setManagementName(getManagement().get(dto.getManagementId()));
//                    }
//                    return dto;
//                })
                .map(this::setEmpGroupDTO)
                .collect(Collectors.toList());
        PageInfo<PrEmpGroupDTO> resultPage = new PageInfo<>(empGroups);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }


    private PrEmpGroupDTO setEmpGroupDTO(PrEmpGroupDTO prEmpGroupDTO){
        if(getManagement().containsKey(prEmpGroupDTO.getManagementId())){
            prEmpGroupDTO.setManagementName(getManagement().get(prEmpGroupDTO.getManagementId()));
        }
        return prEmpGroupDTO;
    }

    @Override
    public JsonResult addEmployeeGroup(@RequestBody PrEmpGroupDTO prEmpGroupDTO) {
        String empGroupCode = codeGenerator.genEmpGroupCode(prEmpGroupDTO.getManagementId());
        prEmpGroupDTO.setEmpGroupCode(empGroupCode);
        prEmpGroupDTO.setCreatedBy("macor");
        prEmpGroupDTO.setModifiedBy("macor");
        PrEmpGroupPO empGroupPO  = EmployeeGroupTranslator.toPrEmpGroupPO(prEmpGroupDTO);

        Integer result = employeeGroupService.addEmployeeGroup(empGroupPO);
        if(result > 0){
            return new JsonResult(true,"添加成功！");
        }
        else
        {
            return new JsonResult(false,"添加失败！");
        }
    }

    @Override
    public JsonResult editEmployeeGroup(@RequestBody PrEmpGroupDTO prEmpGroupDTO) {
        prEmpGroupDTO.setModifiedBy("bill");
        prEmpGroupDTO.setModifiedTime(new Date());
        PrEmpGroupPO empGroupPO  = EmployeeGroupTranslator.toPrEmpGroupPO(prEmpGroupDTO);
        Integer result = employeeGroupService.editEmployeeGroup(empGroupPO);
        if(result > 0){
            return new JsonResult(true,"编辑成功！");
        }
        else
        {
            return new JsonResult(false,"编辑失败！");
        }
    }

    @Override
    public JsonResult getDepart() {
        List<KeyValuePO> keyValues = new ArrayList<>();
        for(Map.Entry<Integer, String> depart:getDepartment().entrySet()){
            KeyValuePO keyValuePO = new KeyValuePO();
            keyValuePO.setKey(String.valueOf(depart.getKey()));
            keyValuePO.setValue(depart.getValue());
            keyValues.add(keyValuePO);
        }
        return JsonResult.success(keyValues);
    }

    @Override
    public JsonResult getPos() {

        List<KeyValuePO> keyValues = new ArrayList<>();
        for(Map.Entry<Integer, String> pos:getPosition().entrySet()){
            KeyValuePO keyValuePO = new KeyValuePO();
            keyValuePO.setKey(String.valueOf(pos.getKey()));
            keyValuePO.setValue(pos.getValue());
            keyValues.add(keyValuePO);
        }
        return JsonResult.success(keyValues);
    }

    @Override
    public JsonResult batchDelete(@PathVariable("ids")String ids) {
        String[] empGroupIds = ids.split(",");
        Boolean success = employeeGroupService.batchDelete( Arrays.asList(empGroupIds));
        return JsonResult.success(success);
    }
//
//    @Override
//    public JsonResult deleteEmployeeGroup(String id) {
//        return null;
//    }
//
//    @Override
//    public JsonResult getEmployeeGroup(String id, String managementId, String empGroupCode, String empGroupName) {
//        return null;
//    }
//
//    @Override
//    public JsonResult getEmployeeGroups(String managementId, String empGroupCode, String empGroupName, Integer pageNum) {
//        return null;
//    }
//


//    @Autowired
//    private PrEmployeeGroupService prEmployeeGroupService;
//
//    @Autowired
//    private EmployeeService prEmployeeService;
//
//    @Autowired
//    private PrEntityIdClient entityIdClient;
//
//    @GetMapping(value = "/importedPrEmployee")
//    public ResultEntity getImportedEmployeeList(@RequestParam String managementId) {
//
//        List<PrEmployeeEntity> resultList = prEmployeeService.getImportedEmployeeList(managementId);
//        return ResultEntity.success(resultList);
//    }
//
//    @GetMapping(value = "/prEmployeeGroup")
//    public ResultEntity getEmpGroupList(@RequestParam String managementId,
//                                                       @RequestParam String companyId,
//                                                       @RequestParam(required = false, defaultValue = "1") Integer pageNum) {
//
//        PrEmployeeGroupEntity paramItem = new PrEmployeeGroupEntity();
//        setCommonParam(paramItem, managementId, companyId);
//        PageInfo<PrEmployeeGroupEntity> pageInfo =  prEmployeeGroupService.getList(paramItem, pageNum);
//        List<PrEmployeeGroupEntity> resultList = pageInfo.getList();
//        return ResultEntity.success(resultList);
//    }
//
//    @GetMapping(value = "/prEmployeeGroupName")
//    public ResultEntity getEmployeeNameList(@RequestParam String managementId,
//                                            @RequestParam String companyId) {
//
//        List<String> resultList = prEmployeeGroupService.getNameList(managementId, companyId);
//        return ResultEntity.success(resultList);
//    }
//
//    @PostMapping(value = "/prEmployeeGroup")
//    public ResultEntity newEmployeeGroup(@RequestBody PrEmployeeGroupEntity prEmployeeGroupEntity){
//
//        Random random = new Random();
//        DecimalFormat df5 = new DecimalFormat("00000");
//        if (prEmployeeGroupEntity.getEntityId()==null){
//            prEmployeeGroupEntity.setEntityId(entityIdClient.getEntityId(PrEntityIdClient.PR_EMPLOYEE_CAT_ID));
//        }
//
//        Integer tempCode = random.nextInt(9999);
//        prEmployeeGroupEntity.setCode("GYZ-CMY000001-" + df5.format(tempCode));
//        prEmployeeGroupEntity.setCreatedBy("jiang");
//        prEmployeeGroupEntity.setModifiedBy("jiang");
//        int result = prEmployeeGroupService.addItem(prEmployeeGroupEntity);
//        return ResultEntity.success(result);
//    }
//
//    @GetMapping(value = "/prEmployeeGroup/{id}")
//    public ResultEntity getEmployeeGroup(@PathVariable("id") String id,
//                                   @RequestParam String managementId,
//                                   @RequestParam String companyId){
//
//        PrEmployeeGroupEntity paramEntity = new PrEmployeeGroupEntity();
//        setCommonParam(paramEntity, managementId, companyId);
//        paramEntity.setEntityId(id);
//        PrEmployeeGroupEntity result = prEmployeeGroupService.getItem(paramEntity);
//        return ResultEntity.success(result);
//    }
//
//    @GetMapping(value = "/prEmployee")
//    public ResultEntity getEmployeeList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
//                                                  @RequestParam(required = false, defaultValue = "") String depart,
//                                                  @RequestParam(required = false, defaultValue = "") String position){
//
//        PrEmployeeEntity paramEntity = new PrEmployeeEntity();
//        PageInfo<PrEmployeeEntity> pageInfo = prEmployeeService.getEmployeeList(paramEntity, pageNum);
//        List<PrEmployeeEntity> resultList = pageInfo.getList();
//        return ResultEntity.success(resultList);
//    }
//
//    @PutMapping(value = "/prEmployeeGroup/{id}")
//    public ResultEntity updateEmployeeGroup(@PathVariable("id") String id,
//                                                     @RequestBody PrEmployeeGroupEntity prEmployeeGroupEntity){
//
//        prEmployeeGroupEntity.setEntityId(id);
//        prEmployeeGroupEntity.setCreatedBy("jiang");
//        prEmployeeGroupEntity.setModifiedBy("jiang");
//        int result = prEmployeeGroupService.updateItem(prEmployeeGroupEntity);
//        return ResultEntity.success(result);
//    }
//
//    @PostMapping(value = "/prEmployeeGroupQuery")
//    public ResultEntity prItemTemplateListQuery(@RequestBody PrEmployeeGroupEntity paramItem,
//                                                              @RequestParam(required = false, defaultValue = "1") Integer pageNum) {
//
//        PageInfo<PrEmployeeGroupEntity> pageInfo =  prEmployeeGroupService.getList(paramItem, pageNum);
//        List<PrEmployeeGroupEntity> resultList = pageInfo.getList();
//        return ResultEntity.success(resultList);
//    }
//
//    @DeleteMapping(value = "/prEmployeeGroup/{id}")
//    public ResultEntity deletePrEmployeeGroup(@PathVariable("id") String id){
//        int result = prEmployeeGroupService.deleteItem(id);
//        if (result==1){
//            return ResultEntity.success(result,"删除成功");
//        }else {
//            return ResultEntity.faultMessage();
//        }
//    }
//

    private void setCommonParam(PrEmpGroupPO empGroupPO, String managementId, String empGroupCode, String empGroupName) {
        if (empGroupPO != null) {
            if(!managementId.isEmpty()){
                empGroupPO.setManagementId(managementId);
            }
            if(empGroupCode != null){
                empGroupPO.setEmpGroupCode(empGroupCode);
            }
            if(empGroupName != null){
                empGroupPO.setName(empGroupName);
            }

        } else {

        }
    }
}
