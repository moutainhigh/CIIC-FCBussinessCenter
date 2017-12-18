package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrPayrollGroupDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by houwanhua on 2017/12/5.
 */

@FeignClient("fcoperation-center-command-service")
@RequestMapping("/api/payrollgroup")
public interface PayrollGroupProxy {

    @GetMapping("/import/{id}")
    JsonResult importPayrollGroup(@RequestParam String from,@PathVariable("id") String to);



    @GetMapping("/getpayrollgroup/{id}")
    @ResponseBody
    JsonResult getPayrollGroup(@PathVariable("id") String id,
                               @RequestParam String managementId);


    @GetMapping("/getpayrollgroup")
    @ResponseBody
    JsonResult getPayrollGroups(@RequestParam String managementId,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum);

    @GetMapping("/getpayrollgroup")
    @ResponseBody
    JsonResult getPayrollGroups(@RequestBody PrPayrollGroupDTO payrollGroupDTO,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum);


    @GetMapping("/getpayrollgroupnames")
    @ResponseBody
    JsonResult getPayrollGroupNames(@RequestParam String managementId,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNum);

    @PutMapping("/edit/{id}")
    @ResponseBody
    JsonResult editPayrollGroup(@PathVariable("id") String id,@RequestBody PrPayrollGroupDTO payrollGroupDTO);


    @PostMapping("/add")
    @ResponseBody
    JsonResult addPayrollGroup(@RequestBody PrPayrollGroupDTO payrollGroupDTO);

    @DeleteMapping("/delete/{id}")
    JsonResult deletePayrollGroup(@PathVariable("id") String id);

    @GetMapping("/getpayrollgroupnames")
    @ResponseBody
    JsonResult getPayrollGroupNames(@RequestParam String managementId);
}
