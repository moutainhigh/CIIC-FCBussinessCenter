package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by houwanhua on 2017/12/5.
 */

@FeignClient("fcbusiness-center-compute-service")
@RequestMapping("/api/prAccountSet")
public interface PayrollAccountProxy {

    @RequestMapping("/getAccountSets")
    JsonResult getAccountSets(@RequestParam String managementId,
                                     @RequestParam(required = false, defaultValue = "50") Integer pageSize,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum);

    @RequestMapping("/getPrItemList")
    public JsonResult getPrItemList(@RequestParam String accountSetCode,
                                    @RequestParam(required = false, defaultValue = "50") Integer pageSize,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNum);

}
