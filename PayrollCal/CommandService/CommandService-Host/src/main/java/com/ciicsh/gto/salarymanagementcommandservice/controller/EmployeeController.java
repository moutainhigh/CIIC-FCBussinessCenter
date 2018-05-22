package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmpEmployeeQryRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmpEmployeeResponseDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrEmployeeDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.EmployeeTranslator;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by houwanhua on 2017/12/11.
 * Edit by Neo Jiang
 */
@RestController
@RequestMapping("/api/employee")
public class EmployeeController{

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeServiceProxy employeeServiceProxy;

//    @Override
    @PostMapping("/addEmployees")
    public JsonResult addEmployees(@RequestBody List<String> empIds, @RequestParam String empGroupCode) {
        Boolean success = employeeService.addEmployees(empIds, empGroupCode);
        return JsonResult.success(success);
    }

//    @Override
    @DeleteMapping("/batchDelete")
    public JsonResult batchDelete(@RequestParam String employeeIds,@RequestParam String empGroupCode) {

        Integer value = employeeService.batchDelete(Arrays.asList(employeeIds.split(",")),empGroupCode);
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

        empEmployeeQryRequestDTO.setPageNo(pageNum);
        empEmployeeQryRequestDTO.setPageSize(pageSize);
        JsonResult<Page<EmpEmployeeResponseDTO>> result
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

    @GetMapping("/getEmployeesByGroup")
    public JsonResult getEmployeesByGroup(@RequestParam String groupCode,
                                          @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {

        PageInfo<PrEmployeePO> page = employeeService.getEmployees(groupCode, pageNum, pageSize);
        List<PrEmployeeDTO> resultList = page.getList()
                .stream()
                .map(EmployeeTranslator::toPrEmployeeDTO)
                .collect(Collectors.toList());
        PageInfo<PrEmployeeDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(page, resultPage, "list");
        return JsonResult.success(resultPage);
    }

}
