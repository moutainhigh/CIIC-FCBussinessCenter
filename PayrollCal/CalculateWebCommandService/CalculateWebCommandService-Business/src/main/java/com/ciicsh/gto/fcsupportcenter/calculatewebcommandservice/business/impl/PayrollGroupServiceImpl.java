package com.ciicsh.gto.fcsupportcenter.calculatewebcommandservice.business.impl;

import com.ciicsh.gto.fcsupportcenter.calculatewebcommandservice.business.PayrollGroupService;
import com.ciicsh.gto.fcsupportcenter.calculatewebcommandservice.dao.ProductGroupMapper;
import com.ciicsh.gto.fcsupportcenter.calculatewebcommandservice.entity.po.PayrollGroupPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shenjian on 2017/11/28.
 */
@Service
@Transactional
public class PayrollGroupServiceImpl implements PayrollGroupService{

    @Autowired
    private ProductGroupMapper productgroupMapper;

    @Override
    public PayrollGroupPO getById(String Id) {
        return productgroupMapper.getById("testId");
    }
}
