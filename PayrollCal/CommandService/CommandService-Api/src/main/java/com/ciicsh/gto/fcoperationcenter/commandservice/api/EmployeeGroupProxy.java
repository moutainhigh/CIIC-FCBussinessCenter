package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by houwanhua on 2017/12/5.
 */

@FeignClient("fcbusiness-center-command-service")
@RequestMapping("/api/employeegroup")
public interface EmployeeGroupProxy {
    @GetMapping("/getemployeegroupnames")
    JsonResult getEmployeeGroupNames(@RequestParam String managementId,@RequestParam String query);
    @PostMapping("/add")
    JsonResult addEmployeeGroup( @RequestBody PrEmpGroupDTO prEmpGroupDTO);
//    @GetMapping("/delete/{id}")
//    JsonResult deleteEmployeeGroup(@PathVariable("id")String id);
//    @GetMapping("/getemployeegroup/{id}")
//    JsonResult getEmployeeGroup(@PathVariable("id") String id,
//                                @RequestParam String managementId,
//                                @RequestParam String empGroupCode,
//                                @RequestParam String empGroupName);
//
//
    @PostMapping("/getemployeegroups")
    JsonResult getEmployeeGroups(@RequestBody PrEmpGroupDTO prEmpGroupDTO,
                                 @RequestParam(required = false, defaultValue = "1",value = "pageNum") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "50",value = "pageSize")  Integer pageSize);


    @PostMapping("/edit")
    JsonResult editEmployeeGroup(@RequestBody PrEmpGroupDTO prEmpGroupDTO);


    @GetMapping("/getDepart")
    JsonResult getDepart();

    @GetMapping("/getPos")
    JsonResult getPos();

    @DeleteMapping("/batchDelete/{ids}")
    JsonResult batchDelete(@PathVariable("ids") String ids,@RequestParam String empGroupCodes);
}
