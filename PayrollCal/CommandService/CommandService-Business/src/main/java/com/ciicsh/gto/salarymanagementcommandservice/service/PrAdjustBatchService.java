package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.mongodb.DBObject;

import java.util.List;

/**
 * Created by bill on 18/1/13.
 */
public interface PrAdjustBatchService {

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

    int insert(PrAdjustBatchPO adjustBatchPO);

    List<DBObject> getAdjustBatch(String batchCode, String originCode);

    /**
     * 更新批次状态
     * @param batchCode
     * @param status
     * @return
     */
    int updateBatchStatus(String batchCode, int status, String modifiedBy);

}
