package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by bill on 18/1/13.
 */
@FeignClient("fcbusiness-center-compute-service")
public interface BatchProxy {

    /**
     * 获取一个批次计算结果 BY 批次编号 JSON 格式
     * @param batchCode
     * @param batchType 批次类型：1表示正常，2表示调整，3表示回溯
     * @return
     */
    @GetMapping("/getBatch")
    String getBatchInfoByCode(@RequestParam("batchCode") String batchCode, @RequestParam("batchType") int batchType);

    /**
     * 根据管理方ID获取批次列表
     * @param managementId
     * @return 批次ID列表
     */
    @GetMapping("/getBatchListByManagementId")
    List<String> getBatchListByManagementId(@RequestParam("managementId") String managementId);
}
