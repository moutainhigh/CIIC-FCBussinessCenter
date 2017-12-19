package com.ciicsh.gto.salarymanagementcommandservice.service.impl;


import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountSetMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAccountSetService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/11/1.
 * @author jiangtianning
 */
@Service
public class PrAccountSetServiceImpl implements PrAccountSetService {

    @Autowired
    private PrPayrollAccountSetMapper accountSetMapper;

    @Autowired
    private PrPayrollItemMapper payrollItemMapper;

    @Autowired
    private PrPayrollAccountItemRelationMapper relationMapper;

    @Override
    public Boolean addAccountSet(PrPayrollAccountSetPO payrollAccountSetPO) {
        try {
            Integer val = accountSetMapper.insert(payrollAccountSetPO);
            if(val > 0){
                PayrollGroupExtPO extPO = new PayrollGroupExtPO();
                extPO.setManagementId(payrollAccountSetPO.getManagementId());
                if(payrollAccountSetPO.getIfGroupTemplate()){
                    extPO.setPayrollGroupTemplateCode(payrollAccountSetPO.getPayrollGroupTemplateCode());
                }
                else {
                    extPO.setPayrollGroupCode(payrollAccountSetPO.getPayrollGroupCode());
                }
                List<PrPayrollItemPO> payrollItems = payrollItemMapper.getPayrollItems(extPO);
                if(payrollItems != null && payrollItems.size() > 0){
                    List<PrPayrollAccountItemRelationPO> relations = payrollItems
                            .stream()
                            .map(item->toPayrollAccountItemRelationPO(item,payrollAccountSetPO))
                            .collect(Collectors.toList());
                    if(null != relations && relations.size() > 0){
                        relations.forEach(x->relationMapper.insert(x));
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception ex){
            return false;
        }
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

    private PrPayrollAccountItemRelationPO toPayrollAccountItemRelationPO(PrPayrollItemPO payrollItemPO,PrPayrollAccountSetPO payrollAccountSetPO){
        PrPayrollAccountItemRelationPO relationPO = new PrPayrollAccountItemRelationPO();
        relationPO.setAccountSetCode(payrollAccountSetPO.getAccountSetCode());
        relationPO.setPayrollItemCode(payrollItemPO.getItemCode());
        relationPO.setActive(true);
        relationPO.setCreatedTime(new Date());
        relationPO.setCreatedBy("macor");
        relationPO.setModifiedTime(new Date());
        relationPO.setModifiedBy("macor");
        return relationPO;
    }
}
