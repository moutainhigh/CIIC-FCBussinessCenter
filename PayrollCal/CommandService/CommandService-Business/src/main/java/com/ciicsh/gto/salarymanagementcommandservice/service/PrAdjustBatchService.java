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
     * @param batchCodes
     * @param hasAdvance
     * @param modifiedBy
     * @return
     */
    int updateHasAdvance(List<String> batchCodes, int hasAdvance, String modifiedBy);

    /**
     * 更新批次是否垫付
     * @param batchCodes
     * @param hasMoney
     * @param modifiedBy
     * @return
     */
    int updateHasMoney(List<String> batchCodes, boolean hasMoney, String modifiedBy);

    int insert(PrAdjustBatchPO adjustBatchPO);

    PrAdjustBatchPO getAdjustBatchPO(PrAdjustBatchPO adjustBatchPO);

    List<DBObject> getAdjustBatch(String batchCode);

    /**
     * 更新批次状态
     * @param batchCode
     * @param status
     * @return
     */
    int auditBatch(String batchCode, String comments, int status, String modifiedBy, String result);

    Integer deleteAdjustBatchByCodes(List<String> codes);

    /**
     * 只有所有调整批次状态是薪资已到账，才可以新增调整批次
     * @param originBatchCode
     * @return
     */
    int checkAdjustBatch(String originBatchCode);

}
