package com.ciicsh.gto.fcoperationcenter.commandservice.api;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.AdvanceBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.MoneyBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrNormalBatchDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bill on 18/1/13.
 */
@FeignClient("fcbusiness-center-compute-service")
@RequestMapping(value = "/api/prBatch")
public interface BatchProxy {

    /**
     *
     * @param advanceBatchDTO
     * @return
     */
    @PostMapping("/updateAdvanceBatch")
    int updateAdvanceBatch(@RequestBody AdvanceBatchDTO advanceBatchDTO);


    /**
     *
     * @param moneyBatchDTO
     * @return
     */
    @PostMapping("/updateHasMoneyBatch")
    int updateHasMoneyBatch(@RequestBody MoneyBatchDTO moneyBatchDTO);

    /**
     * 根据管理方ID获取批次列表
     * @param managementId
     * @return 批次ID列表
     */
    @GetMapping("/getBatchListByManagementId")
    List<PrNormalBatchDTO> getBatchListByManagementId(@RequestParam("managementId") String managementId);

    /**
     * 获取一个批次计算结果 BY 批次编号 JSON 格式
     * @param batchCode
     * @param batchType 批次类型：1表示正常，2表示调整，3表示回溯
     * @return
     */
    @GetMapping("/getBatchInfo")
    PrBatchDTO getBatchInfo(@RequestParam("batchCode") String batchCode, @RequestParam("batchType") int batchType);
}
