package com.ciicsh.gto.salarymanagementcommandservice.api;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.AccountSetWithItemsDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/5.
 */

@FeignClient("fcbusiness-center-compute-service")
@RequestMapping("/api/prAccountSet")
public interface PayrollAccountProxy {
    @GetMapping("/getAccountSetsByManagementId")
    List<AccountSetWithItemsDTO> getAccountSetsByManagementId(@RequestParam("managementId") String managementId);
}
