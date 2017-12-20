package com.ciicsh.gto.salarymanagementcommandservice.service.impl;


import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountSetMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAccountSetService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/11/1.
 * @author jiangtianning
 */
@Service
public class PrAccountSetServiceImpl implements PrAccountSetService {

    @Autowired
    private PrPayrollAccountSetMapper accountSetMapper;

    static final private int PAGE_SIZE = 10;

    @Override
    public Integer addAccountSet(PrPayrollAccountSetPO payrollAccountSetPO) {
        return accountSetMapper.insert(payrollAccountSetPO);
    }

    @Override
    public List<KeyValuePO> getPayrollAccountSetNames(String managementId) {
        return accountSetMapper.getPayrollAccountSetNames(managementId);
    }

    @Override
    public PageInfo<PrPayrollAccountSetPO> getPayrollAccountSets(PrPayrollAccountSetPO payrollAccountSetPO, Integer pageNum, Integer pageSize) {
        List<PrPayrollAccountSetPO> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        try {
            resultList = accountSetMapper.getPayrollAccountSets(payrollAccountSetPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrPayrollAccountSetPO> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public PageInfo<PrPayrollAccountSetExtensionPO> getPayrollAccountSetExts(PrPayrollAccountSetExtensionPO extensionPO, Integer pageNum, Integer pageSize) {
        List<PrPayrollAccountSetExtensionPO> results = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        try {
            results = accountSetMapper.getPayrollAccountSetExts(extensionPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrPayrollAccountSetExtensionPO> pageInfo = new PageInfo<>(results);
        return  pageInfo;
    }

    @Override
    public PrPayrollAccountSetPO getAccountSetInfo(String accSetCode) {
        return accountSetMapper.getAccountSetInfo(accSetCode);
    }
}
