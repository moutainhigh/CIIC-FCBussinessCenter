package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.po.PrBatchExcelMapPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrBatchExcelMapMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBatchExcelMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 18/5/14.
 */
@Service
public class PrBatchExcelMapServiceImpl implements PrBatchExcelMapService {

    @Autowired
    private PrBatchExcelMapMapper batchExcelMapMapper;

    @Override
    public PrBatchExcelMapPO getBatchExcelMap(String batchCode) {
        PrBatchExcelMapPO prBatchExcelMapPO = new PrBatchExcelMapPO();
        prBatchExcelMapPO.setBatchCode(batchCode);
        return batchExcelMapMapper.selectOne(prBatchExcelMapPO);
    }

    @Override
    public int insert(PrBatchExcelMapPO prBatchExcelMapPO) {
        return batchExcelMapMapper.insertBatchExcelMap(prBatchExcelMapPO);
    }

    @Override
    public int update(PrBatchExcelMapPO prBatchExcelMapPO) {
        return batchExcelMapMapper.updateBatchExcelMap(prBatchExcelMapPO);
    }
}
