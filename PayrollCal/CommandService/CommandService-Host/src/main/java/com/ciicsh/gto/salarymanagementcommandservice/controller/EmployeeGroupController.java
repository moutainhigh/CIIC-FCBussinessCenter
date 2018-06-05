package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.salarymanagementcommandservice.api.EmployeeGroupProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.EmpGroupOptPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeGroupTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/10/25.
 */
@RestController
@RequestMapping("/api/employeegroup")
public class EmployeeGroupController extends BaseController implements EmployeeGroupProxy{

    @Autowired
    private EmployeeGroupService employeeGroupService;

    @GetMapping("/getemployeegroupnames")
    public JsonResult getEmployeeGroupNames(@RequestParam String managementId) {
        List<KeyValuePO> keyValues = employeeGroupService.getEmployeeGroupNames(managementId);
        return JsonResult.success(keyValues);
    }

    @PostMapping("/getemployeegroups")
    public JsonResult getEmployeeGroups(@RequestBody PrEmpGroupDTO prEmpGroupDTO,
                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {
        PrEmpGroupPO empGroupPO  = EmployeeGroupTranslator.toPrEmpGroupPO(prEmpGroupDTO);
        empGroupPO.setName(prEmpGroupDTO.getEmpGroupName());
        empGroupPO.setManagementId(CommonHelper.getManagementIDs());
        PageInfo<PrEmpGroupPO> pageInfo =  employeeGroupService.getEmployeeGroups(empGroupPO, pageNum,pageSize);
        List<PrEmpGroupDTO> empGroups = pageInfo.getList()
                .stream()
                .map(EmployeeGroupTranslator::toPrEmpGroupDTO)
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

    @PostMapping("/add")
    public JsonResult addEmployeeGroup(@RequestBody PrEmpGroupDTO prEmpGroupDTO) {

        EmpGroupOptPO optPO = new EmpGroupOptPO();
        optPO.setManagementId(prEmpGroupDTO.getManagementId());
        optPO.setEmpGroupName(prEmpGroupDTO.getName());
        Integer val = employeeGroupService.isExistEmpGroup(optPO);
        if(val > 0){
            return JsonResult.faultMessage("添加失败,已经存在在同一个管理方下相同的雇员组，请检查！");
        }
        else {
            String empGroupCode = codeGenerator.genEmpGroupCode(prEmpGroupDTO.getManagementId());
            prEmpGroupDTO.setEmpGroupCode(empGroupCode);
            prEmpGroupDTO.setCreatedBy(UserContext.getUserId());
            prEmpGroupDTO.setModifiedBy(UserContext.getUserId());
            PrEmpGroupPO empGroupPO  = EmployeeGroupTranslator.toPrEmpGroupPO(prEmpGroupDTO);

            Integer result = employeeGroupService.addEmployeeGroup(empGroupPO);
            if(result > 0){

                PrEmpGroupPO groupPO = employeeGroupService.getEmployeeGroupByCode(empGroupCode);
                PrEmpGroupDTO groupDTO = null;
                if(null != groupPO){
                    groupDTO = EmployeeGroupTranslator.toPrEmpGroupDTO(groupPO);
                    if(getManagement().containsKey(groupDTO.getManagementId())){
                        groupDTO.setManagementName(getManagement().get(groupDTO.getManagementId()));
                    }
                }
                return JsonResult.success(groupDTO,"添加成功！");
            }
            else
            {
                return JsonResult.faultMessage("添加失败！");
            }
        }
    }

    @PostMapping("/edit")
    public JsonResult editEmployeeGroup(@RequestBody PrEmpGroupDTO prEmpGroupDTO) {
        EmpGroupOptPO optPO = new EmpGroupOptPO();
        optPO.setManagementId(prEmpGroupDTO.getManagementId());
        optPO.setEmpGroupName(prEmpGroupDTO.getName());
        optPO.setEmpGroupCode(prEmpGroupDTO.getEmpGroupCode());
        Integer val = employeeGroupService.isExistEmpGroup(optPO);
        if(val > 0){
            return JsonResult.faultMessage("编辑失败,已经存在在同一个管理方下相同的雇员组，请检查！");
        }
        else{
            prEmpGroupDTO.setModifiedBy("bill");
            prEmpGroupDTO.setModifiedTime(new Date());
            PrEmpGroupPO empGroupPO  = EmployeeGroupTranslator.toPrEmpGroupPO(prEmpGroupDTO);
            Integer result = employeeGroupService.editEmployeeGroup(empGroupPO);
            if(result > 0){
                PrEmpGroupPO groupPO = employeeGroupService.getEmployeeGroupByCode(prEmpGroupDTO.getEmpGroupCode());
                PrEmpGroupDTO groupDTO = null;
                if(null != groupPO){
                    groupDTO = EmployeeGroupTranslator.toPrEmpGroupDTO(groupPO);
                    if(getManagement().containsKey(groupDTO.getManagementId())){
                        groupDTO.setManagementName(getManagement().get(groupDTO.getManagementId()));
                    }
                }
                return JsonResult.success(groupDTO,"编辑成功！");
            }
            else
            {
                return JsonResult.faultMessage("编辑失败！");
            }
        }
    }
//
//    @GetMapping("/getDepart")
//    public JsonResult getDepart() {
//        List<KeyValuePO> keyValues = new ArrayList<>();
//        for(Map.Entry<Integer, String> depart:getDepartment().entrySet()){
//            KeyValuePO keyValuePO = new KeyValuePO();
//            keyValuePO.setKey(String.valueOf(depart.getKey()));
//            keyValuePO.setValue(depart.getValue());
//            keyValues.add(keyValuePO);
//        }
//        return JsonResult.success(keyValues);
//    }
//
//    @GetMapping("/getPos")
//    public JsonResult getPos() {
//
//        List<KeyValuePO> keyValues = new ArrayList<>();
//        for(Map.Entry<Integer, String> pos:getPosition().entrySet()){
//            KeyValuePO keyValuePO = new KeyValuePO();
//            keyValuePO.setKey(String.valueOf(pos.getKey()));
//            keyValuePO.setValue(pos.getValue());
//            keyValues.add(keyValuePO);
//        }
//        return JsonResult.success(keyValues);
//    }

    @DeleteMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids")String ids,@RequestParam String empGroupCodes) {
        String[] empGroupIds = ids.split(",");
        String[] codes = empGroupCodes.split(",");
        Boolean success = employeeGroupService.batchDelete( Arrays.asList(empGroupIds),Arrays.asList(codes));
        return JsonResult.success(success);
    }

    @GetMapping("/{empGroupCode}")
    public JsonResult getEmployeeGroupById(@PathVariable("empGroupCode") String empGroupCode) {
        PrEmpGroupPO empGroupPO = employeeGroupService.getEmployeeGroupByCode(empGroupCode);
        PrEmpGroupDTO empGroupDTO = EmployeeGroupTranslator.toPrEmpGroupDTO(empGroupPO);
        //set management name -- added by bill 2018-05-04
        Optional<ManagementInfo> find = UserContext.getManagementInfoLists().stream().filter(mgrInfo -> mgrInfo.getManagementId().equals(empGroupDTO.getManagementId())).findFirst();
        if(find.isPresent()){
            empGroupDTO.setManagementName(find.get().getManagementName());
        }
        return JsonResult.success(empGroupDTO);
    }
}
