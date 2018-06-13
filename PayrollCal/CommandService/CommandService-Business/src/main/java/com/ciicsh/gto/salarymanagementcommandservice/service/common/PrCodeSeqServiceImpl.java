package com.ciicsh.gto.salarymanagementcommandservice.service.common;

import com.ciicsh.gto.salarymanagementcommandservice.dao.PrCodeSequenceMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.PrCodeSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiangtianning on 2017/11/23.
 * @author jiangtianning
 */
@Service
public class PrCodeSeqServiceImpl implements PrCodeSeqService {

    @Autowired
    PrCodeSequenceMapper prCodeSequenceMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRES_NEW)
    public long getCode(String seqName) {
        return prCodeSequenceMapper.selectSeq(seqName);
    }
}
