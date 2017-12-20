package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrPayrollAccountItemRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/20.
 */
@Service
public class PrPayrollAccountItemRelationServiceImpl implements PrPayrollAccountItemRelationService {

    @Autowired
    private PrPayrollAccountItemRelationMapper relationMapper;

    @Override
    public List<PayrollAccountItemRelationExtPO> getAccountItemRelationExts(String accountSetCode) {
        return relationMapper.getAccountItemRelationExts(accountSetCode);
    }
}
