package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.EmployeeProxy;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.EmployeeExtensionDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmployeeTestDTO;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeTestPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeTestTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeTranslator;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by houwanhua on 2017/12/11.
 */
@RestController
public class EmployeeController implements EmployeeProxy{

    @Autowired
    private EmployeeService employeeService;

    @Override
    public JsonResult addEmployees(@RequestBody List<PrEmployeeTestDTO> employeeTestDTOS, @RequestParam String empGroupId) {

        List<PrEmployeeTestPO> employeeTestPOS = employeeTestDTOS.stream()
                .map(EmployeeTestTranslator::toPrEmployeeTestPO)
                .collect(Collectors.toList());
        Boolean success = employeeService.addEmployees(employeeTestPOS,empGroupId);
        return JsonResult.success(success);
    }

    @Override
    public JsonResult getEmployees(@RequestParam String empGroupId,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {
        PageInfo<EmployeeExtensionPO> pageInfo =  employeeService.getEmployees(Integer.parseInt(empGroupId), pageNum,pageSize);
        List<EmployeeExtensionDTO> employeeExtensions = pageInfo.getList()
                .stream()
                .map(EmployeeTranslator::toEmployeeExtensionDTO)
                .collect(Collectors.toList());

        PageInfo<EmployeeExtensionDTO> result = new PageInfo<>(employeeExtensions);
        BeanUtils.copyProperties(pageInfo,result,"list");
        return JsonResult.success(result);
    }

    @Override
    public JsonResult batchDelete(@PathVariable("ids")String ids,@RequestParam String employeeIds,@RequestParam String empGroupId) {
        String[] relationIds = ids.split(",");
        Integer value = employeeService.batchDelete(Arrays.asList(relationIds),Arrays.asList(employeeIds),empGroupId);
        if(value > 0){
            return JsonResult.success(value,"删除成功!");
        }
        else{
            return JsonResult.faultMessage();
        }
    }
}
