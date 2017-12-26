package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmployeeTestDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/5.
 */
@FeignClient("fcoperation-center-command-service")
@RequestMapping("/api/employee")
public interface EmployeeProxy {

//    @GetMapping("/getemployees")
//    JsonResult getEmployees(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
//                            @RequestParam(required = false, defaultValue = "") String depart,
//                            @RequestParam(required = false, defaultValue = "") String position);
//    @GetMapping("/importemployee")
//    JsonResult getImportedEmployeeList(@RequestParam String managementId);
    @PostMapping("/addEmployees")
    JsonResult addEmployees(@RequestBody List<PrEmployeeTestDTO> employeeTestDTOS,@RequestParam String empGroupCode);


    @PostMapping("/getEmployees")
    JsonResult getEmployees(@RequestParam String empGroupCode,
                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                            @RequestParam(required = false, defaultValue = "50")  Integer pageSize);


    @DeleteMapping("/batchDelete/{ids}")
    JsonResult batchDelete(@PathVariable("ids") String ids,@RequestParam String employeeIds,@RequestParam String empGroupCode);
}
