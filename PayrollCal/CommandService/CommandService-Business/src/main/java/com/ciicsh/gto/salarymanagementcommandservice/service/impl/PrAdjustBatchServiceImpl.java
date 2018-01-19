package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagementcommandservice.dao.PrAdjustBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 18/1/13.
 */
@Service
public class PrAdjustBatchServiceImpl implements PrAdjustBatchService {

    @Autowired
    private PrAdjustBatchMapper adjustBatchMapper;

    @Override
    public int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy) {
        return adjustBatchMapper.updateHasAdvance(batchCode,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy) {
        return adjustBatchMapper.updateHasMoneny(batchCode,hasMoney,modifiedBy);
    }
}
