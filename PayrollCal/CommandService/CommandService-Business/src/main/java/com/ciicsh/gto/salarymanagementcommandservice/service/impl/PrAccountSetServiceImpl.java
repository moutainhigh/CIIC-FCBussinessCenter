package com.ciicsh.gto.salarymanagementcommandservice.service.impl;


import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountSetOptPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountSetMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAccountSetService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addAccountSet(PrPayrollAccountSetPO payrollAccountSetPO) {
        try {
            Integer val = accountSetMapper.insert(payrollAccountSetPO);
            if(val > 0){
                this.saveItemRelation(payrollAccountSetPO);
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception ex){
            throw new BusinessException("添加薪资帐套异常");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean editAccountSet(PrPayrollAccountSetPO payrollAccountSetPO) {
        try{
            PrPayrollAccountSetExtensionPO extensionPO = accountSetMapper.getPayrollAccountSetExtByCode(payrollAccountSetPO.getAccountSetCode());
            Integer val = accountSetMapper.updateById(payrollAccountSetPO);
            if(val > 0){
                if(extensionPO != null){
                    if(payrollAccountSetPO.getIfGroupTemplate()){
                        if(extensionPO.getIfGroupTemplate()){
                            if(!payrollAccountSetPO.getPayrollGroupTemplateCode().equals(extensionPO.getPayrollGroupTemplateCode())){
                                relationMapper.delAccountItemRelationByAccountCode(payrollAccountSetPO.getAccountSetCode());
                                this.saveItemRelation(payrollAccountSetPO);
                            }
                        }
                        else {
                            relationMapper.delAccountItemRelationByAccountCode(payrollAccountSetPO.getAccountSetCode());
                            this.saveItemRelation(payrollAccountSetPO);
                        }
                    }
                    else{
                        if(extensionPO.getIfGroupTemplate()){
                            relationMapper.delAccountItemRelationByAccountCode(payrollAccountSetPO.getAccountSetCode());
                            this.saveItemRelation(payrollAccountSetPO);
                        }
                        else{
                            if(!payrollAccountSetPO.getPayrollGroupCode().equals(extensionPO.getPayrollGroupCode())){
                                relationMapper.delAccountItemRelationByAccountCode(payrollAccountSetPO.getAccountSetCode());
                                this.saveItemRelation(payrollAccountSetPO);
                            }
                        }
                    }
                }
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception ex){
            return false;
        }
    }

    @Override
    public Integer editIsActive(PrPayrollAccountSetPO payrollAccountSetPO) {
        return accountSetMapper.updateById(payrollAccountSetPO);
    }

    private void saveItemRelation(PrPayrollAccountSetPO payrollAccountSetPO){
        PayrollGroupExtPO extPO = new PayrollGroupExtPO();
        if(payrollAccountSetPO.getIfGroupTemplate()){
            //extPO.setManagementId("GLF-00000");
            extPO.setPayrollGroupTemplateCode(payrollAccountSetPO.getPayrollGroupTemplateCode());
        }
        else {
            //extPO.setManagementId(payrollAccountSetPO.getManagementId());
            extPO.setPayrollGroupCode(payrollAccountSetPO.getPayrollGroupCode());
        }
        List<PrPayrollItemPO> payrollItems = payrollItemMapper.getPayrollItems(extPO);
        if(payrollItems != null && payrollItems.size() > 0){
            List<PrPayrollAccountItemRelationPO> relations = payrollItems
                    .stream()
                    .map(item->toPayrollAccountItemRelationPO(item,payrollAccountSetPO))
                    .collect(Collectors.toList());
            if(null != relations && relations.size() > 0){
                relationMapper.insertAccountItemRelationList(relations);
//                relations.forEach(x->relationMapper.insert(x));
            }
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
        return new PageInfo<>(resultList);
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
        return new PageInfo<>(results);
    }

    @Override
    public PrPayrollAccountSetPO getAccountSetInfo(String accSetCode) {
        return accountSetMapper.getAccountSetInfo(accSetCode);
    }

    @Override
    public Integer isExistPayrollAccountSet(PrAccountSetOptPO optPO) {
        return accountSetMapper.isExistPayrollAccountSet(optPO);
    }

    @Override
    public PrPayrollAccountSetExtensionPO getPayrollAccountSetExtByCode(String accountSetCode) {
        return accountSetMapper.getPayrollAccountSetExtByCode(accountSetCode);
    }

    @Override
    public List<PrAccountSetOptPO> getAccountSetWithItemsByManagementId(String managementId) {
        return accountSetMapper.selectAccountSetWithItemsByManagementId(managementId);
    }

    private PrPayrollAccountItemRelationPO toPayrollAccountItemRelationPO(PrPayrollItemPO payrollItemPO,PrPayrollAccountSetPO payrollAccountSetPO){
        PrPayrollAccountItemRelationPO relationPO = new PrPayrollAccountItemRelationPO();
        relationPO.setAccountSetCode(payrollAccountSetPO.getAccountSetCode());
        relationPO.setPayrollItemCode(payrollItemPO.getItemCode());
        relationPO.setPayrollItemAlias(payrollItemPO.getItemName());
        relationPO.setActive(true);
        relationPO.setCreatedTime(new Date());
        relationPO.setCreatedBy(UserContext.getUserId());
        relationPO.setModifiedTime(new Date());
        relationPO.setModifiedBy(UserContext.getUserId());
        return relationPO;
    }
}
