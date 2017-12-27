package com.ciicsh.gto.fcsupportcenter.companyqueryservice.business.impl;


import com.ciicsh.gto.fcsupportcenter.companyqueryservice.business.PayrollGroupService;
import com.ciicsh.gto.fcsupportcenter.companyqueryservice.dao.ProductGroupMapper;
import com.ciicsh.gto.fcsupportcenter.companyqueryservice.entity.po.PayrollGroupPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shenjian on 2017/11/28.
 */
@Service
@Transactional
public class PayrollGroupServiceImpl implements PayrollGroupService {

    @Autowired
    private ProductGroupMapper productgroupMapper;

    @Override
    public PayrollGroupPO getById(String Id) {
        return productgroupMapper.getById("testId");
    }
}
