package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrBatchExcelMapPO;

/**
 * Created by bill on 18/5/14.
 */
public interface PrBatchExcelMapService {

    PrBatchExcelMapPO getBatchExcelMap(String batchCode);

    int insert(PrBatchExcelMapPO prBatchExcelMapPO);

    int update(PrBatchExcelMapPO prBatchExcelMapPO);

    int deleteBatchExcel(String batchCode);
}
