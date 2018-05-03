package com.ciicsh.gto.salarymanagementcommandservice.api;

import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.AdvanceBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.BatchAuditDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.MoneyBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.page.Pagination;
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
     * 更新垫付
     * @param advanceBatchDTO
     * @return
     */
    @PostMapping("/updateAdvanceBatch")
    int updateAdvanceBatch(@RequestBody AdvanceBatchDTO advanceBatchDTO);


    /**
     * 更新来款
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
    Pagination<PrNormalBatchDTO> getBatchListByManagementId(@RequestParam(value = "managementId", required = false) String managementId,
                                                            @RequestParam(value = "batchCode", required = false) String batchCode,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     * 获取一个批次计算结果 BY 批次编号 JSON 格式
     * @param batchCode
     * @param batchType 批次类型：1表示正常，2表示调整，3表示回溯
     * @return
     */
    @GetMapping("/getBatchInfo")
    PrBatchDTO getBatchInfo(@RequestParam("batchCode") String batchCode, @RequestParam("batchType") int batchType);

    /**
     * 更新批次状态
     * @param batchAuditDTO
     * @return
     */
    @PostMapping("/updateBatchStatus")
    int updateBatchStatus(@RequestBody BatchAuditDTO batchAuditDTO);

    /**
     * 获取批量列表ID 状态：未来款，未垫付，帐套已关闭
     * @param mgrId
     * @return
     */
    @GetMapping("/getBatchIdListByManagementId")
    JsonResult<List<String>> getBatchIdListByManagementId(@RequestParam("mgrId") String mgrId);
}
