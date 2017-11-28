package com.ciicsh.gto.fcsupportcenter.employeequeryservice.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shenjian on 2017/11/28.
 */

@FeignClient("fcsupport-center-calculate-web-service")
@RequestMapping("/webService")
public interface PayrollGroupProxy {
}
