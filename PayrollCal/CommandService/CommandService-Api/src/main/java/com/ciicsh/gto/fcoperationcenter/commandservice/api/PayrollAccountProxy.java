package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by houwanhua on 2017/12/5.
 */

@FeignClient("fcbusiness-center-command-service")
@RequestMapping("/api/payrollaccount")
public interface PayrollAccountProxy {

    @GetMapping("/getpayrollaccountsets")
    JsonResult getPayrollAccountSets(@RequestParam String managementId,
                                   @RequestParam Integer pageNum);

}
