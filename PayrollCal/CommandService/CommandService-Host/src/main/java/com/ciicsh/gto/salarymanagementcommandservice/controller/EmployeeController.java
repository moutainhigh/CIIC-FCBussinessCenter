package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmpEmployeeQryRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmpEmployeeResponseDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.EmployeeProxy;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmployeeTestDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeTestTranslator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private EmployeeServiceProxy employeeServiceProxy;

    @Override
    public JsonResult addEmployees(@RequestBody List<PrEmployeeTestDTO> employeeTestDTOS, @RequestParam String empGroupCode) {

        List<PrEmployeePO> employeeTestPOS = employeeTestDTOS.stream()
                .map(EmployeeTestTranslator::toPrEmployeeTestPO)
                .collect(Collectors.toList());
        Boolean success = employeeService.addEmployees(employeeTestPOS,empGroupCode);
        return JsonResult.success(success);
    }

//    @Override
//    public JsonResult getEmployees(@RequestParam String empGroupCode,
//                                   @RequestParam(required = false, defaultValue = "") String empCode,
//                                   @RequestParam(required = false, defaultValue = "") String empName,
//                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
//                                   @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {
//        PageInfo<EmployeeExtensionPO> pageInfo =  employeeService.getEmployees(empGroupCode,empCode,empName,pageNum,pageSize);
//        List<EmployeeExtensionDTO> employeeExtensions = pageInfo.getList()
//                .stream()
//                .map(EmployeeTranslator::toEmployeeExtensionDTO)
//                .collect(Collectors.toList());
//
//        PageInfo<EmployeeExtensionDTO> result = new PageInfo<>(employeeExtensions);
//        BeanUtils.copyProperties(pageInfo, result,"list");
//        return JsonResult.success(result);
//    }

    @Override
    public JsonResult batchDelete(@PathVariable("ids")String ids,@RequestParam String employeeIds,@RequestParam String empGroupCode) {
        String[] relationIds = ids.split(",");
        Integer value = employeeService.batchDelete(Arrays.asList(relationIds),Arrays.asList(employeeIds),empGroupCode);
        if(value > 0){
            return JsonResult.success(value,"删除成功!");
        }
        else{
            return JsonResult.faultMessage();
        }
    }

    @PostMapping("/getEmployees")
    public JsonResult getEmployees(@RequestBody EmpEmployeeQryRequestDTO empEmployeeQryRequestDTO,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {

//        EmpEmployeeQryRequestDTO param = new EmpEmployeeQryRequestDTO();
        empEmployeeQryRequestDTO.setPageNo(pageNum);
        empEmployeeQryRequestDTO.setPageSize(pageSize);
        com.ciicsh.gto.companycenter.webcommandservice.api.JsonResult<Page<EmpEmployeeResponseDTO>> result
                = employeeServiceProxy.PageEmpEmployee(empEmployeeQryRequestDTO);
        Page<EmpEmployeeResponseDTO> data = result.getData();

        if (data != null && data.getRecords() != null && data.getRecords().size() != 0) {
            data.getRecords().forEach(i -> {
                PrEmployeePO param = new PrEmployeePO();
                BeanUtils.copyProperties(i, param);
                employeeService.upsertEmployee(param);
            });
        }

        return JsonResult.success(data);
    }
}
