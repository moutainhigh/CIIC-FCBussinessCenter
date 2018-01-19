package com.ciicsh.gto.salarymanagementcommandservice.service;

import org.springframework.stereotype.Component;

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

}
