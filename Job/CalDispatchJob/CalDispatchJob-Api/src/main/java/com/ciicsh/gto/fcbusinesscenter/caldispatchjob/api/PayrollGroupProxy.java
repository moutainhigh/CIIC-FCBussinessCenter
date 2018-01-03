package com.ciicsh.gto.fcbusinesscenter.caldispatchjob.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shenjian on 2017/11/28.
 */

@FeignClient("fcbusinesscenter-center-calculate-web-service")
@RequestMapping("/webService")
public interface PayrollGroupProxy {
}
