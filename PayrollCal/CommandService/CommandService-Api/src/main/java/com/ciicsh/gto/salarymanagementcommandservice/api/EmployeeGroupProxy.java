package com.ciicsh.gto.salarymanagementcommandservice.api;

import org.springframework.cloud.netflix.feign.FeignClient;
/**
 * Created by houwanhua on 2017/12/5.
 */

@FeignClient("fcbusiness-center-compute-service")
public interface EmployeeGroupProxy {

}
