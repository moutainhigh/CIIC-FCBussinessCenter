package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagementcommandservice.dao.PrBackTrackingBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 18/1/13.
 */
@Service
public class PrBackTrackingBatchServiceImpl implements PrBackTrackingBatchService {

    @Autowired
    private PrBackTrackingBatchMapper backTrackingBatchMapper;

    @Override
    public int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy) {
        return backTrackingBatchMapper.updateHasAdvance(batchCode,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy) {
        return backTrackingBatchMapper.updateHasAdvance(batchCode,hasMoney,modifiedBy);
    }
}
