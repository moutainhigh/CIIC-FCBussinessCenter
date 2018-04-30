package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NeoJiang on 2018/4/25.
 */
@FeignClient("fcbusiness-center-compute-service")
@RequestMapping("/api/salaryManagement")
public interface PayrollGroupProxy {

    @GetMapping("/prGroupName")
    JsonResult<List<HashMap<String, String>>> getPayrollGroupNameList(@RequestParam("query") String query,
                                              @RequestParam(value = "managementId", required = false, defaultValue = "") String managementId);
}
