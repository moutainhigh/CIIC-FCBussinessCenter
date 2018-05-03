package com.ciicsh.gto.salarymanagementcommandservice.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by houwanhua on 2017/12/5.
 */
@FeignClient("fcbusiness-center-compute-service")
@RequestMapping("/api/employee")
public interface EmployeeProxy {

//    @GetMapping("/getemployees")
//    JsonResult getEmployees(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
//                            @RequestParam(required = false, defaultValue = "") String depart,
//                            @RequestParam(required = false, defaultValue = "") String position);
//    @GetMapping("/importemployee")
//    JsonResult getImportedEmployeeList(@RequestParam String managementId);

//    /**
//     * 添加测试雇员方法
//     * @return
//     */
//    @PostMapping("/addEmployees")
//    JsonResult addEmployees(@RequestBody List<PrEmployeeTestDTO> employeeTestDTOS,@RequestParam String empGroupCode);

//
//    @PostMapping("/getEmployees")
//    JsonResult getEmployees(@RequestParam String empGroupCode,
//                            @RequestParam(required = false, defaultValue = "") String empCode,
//                            @RequestParam(required = false, defaultValue = "") String empName,
//                            @RequestParam(required = false, defaultValue = "1",value = "pageNum") Integer pageNum,
//                            @RequestParam(required = false, defaultValue = "50",value = "pageSize")  Integer pageSize);


//    @DeleteMapping("/batchDelete/{ids}")
//    JsonResult batchDelete(@PathVariable("ids") String ids,@RequestParam String employeeIds,@RequestParam String empGroupCode);
}
