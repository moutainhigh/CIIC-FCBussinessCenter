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
     * 获取多个批次计算结果 BY 批次编号 JSON 格式
     * @param batchCodes
     * @param batchType 批次类型：1表示正常，2表示调整，3表示回溯
     * @return
     */
    String getBatchListByCodes(List<String> batchCodes, int batchType);

    /**
     * 设置该批次状态：true 已垫付、 false 未垫付
     * @param batchCode
     * @param hasAdvance
     * @param batchType 批次类型：1表示正常，2表示调整，3表示回溯
     * @return
     */
    int updateAdvanceBatch(String batchCode, Boolean hasAdvance, int batchType);

    /**
     * 设置该批次状态：true 已来款  false 未来款
     * @param batchCode
     * @param hasMoney
     * @param batchType 批次类型：1表示正常，2表示调整，3表示回溯
     * @return
     */
    int updateHasMoneyBatch(String batchCode, Boolean hasMoney, int batchType);

    /**
     * 根据管理方ID获取批次列表
     * @param managementId
     * @return 批次ID列表
     */
    @GetMapping("/getBatchListByManagementId")
    List<String> getBatchListByManagementId(@RequestParam("managementId") String managementId);
}
