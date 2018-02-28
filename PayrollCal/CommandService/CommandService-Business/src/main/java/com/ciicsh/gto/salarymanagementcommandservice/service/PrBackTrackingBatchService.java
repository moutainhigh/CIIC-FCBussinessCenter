package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import com.mongodb.DBObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by bill on 18/1/13.
 */
public interface PrBackTrackingBatchService {

    /**
     * 更新批次是否来款
     * @param batchCode
     * @param hasAdvance
     * @param modifiedBy
     * @return
     */
    int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy);

    /**
     * 更新批次是否垫付
     * @param batchCode
     * @param hasMoney
     * @param modifiedBy
     * @return
     */
    int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy);

    int insert(PrBackTrackingBatchPO prBackTrackingBatchPO);

    List<DBObject> getBackTrackingBatch(String batchCode, String originCode);
    /**
     * 更新批次状态
     * @param batchCode
     * @param status
     * @return
     */
    int auditBatch(String batchCode, String comments, int status, String modifiedBy, String result);

    Integer deleteBackTraceBatchByCodes(List<String> codes);

    int checkBackTraceBatch(String originBatchCode);

}
