package com.ciicsh.gto.salarymanagementcommandservice.api;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by NeoJiang on 2018/4/25.
 */
@FeignClient("fcbusiness-center-compute-service")
@RequestMapping("/api/salaryManagement")
public interface PayrollGroupProxy {

    @GetMapping("/prGroupName")
    JsonResult getPayrollGroupNameList(@RequestParam("query") String query,
                                              @RequestParam(value = "managementId", required = false, defaultValue = "") String managementId);
}
