package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagementcommandservice.dao.PrCodeSequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiangtianning on 2017/11/23.
 * @author jiangtianning
 */
@Service
public class PrCodeSeqServiceImpl implements com.ciicsh.gto.salarymanagementcommandservice.service.PrCodeSeqService {

    @Autowired
    PrCodeSequenceMapper prCodeSequenceMapper;

    @Override
    public long getCode(String seqName) {
        return prCodeSequenceMapper.selectSeq(seqName);
    }
}
