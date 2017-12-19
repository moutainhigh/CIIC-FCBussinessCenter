package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollEmpGroup;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupEmpRelationPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupEmpRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupMapper;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaSender;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/25.
 */
@Service
public class EmployeeGroupServiceImpl implements EmployeeGroupService {

    @Autowired
    private PrEmpGroupMapper empGroupMapper;

    @Autowired
    private PrEmpGroupEmpRelationMapper empGroupEmpRelationMapper;

    @Autowired
    private KafkaSender sender;


    @Override
    public List<KeyValuePO> getEmployeeGroupNames(String managementId) {
        List<KeyValuePO> keyValues = new ArrayList<>();
        try {
            keyValues = empGroupMapper.getEmployeeGroupNames(managementId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyValues;
    }

    @Override
    public Integer addEmployeeGroup(PrEmpGroupPO prEmpGroupPO) {
        return empGroupMapper.insert(prEmpGroupPO);
    }

    @Override
    public Integer deleteEmployeeGroup(Integer id) {
        PrEmpGroupPO empGroupPO = new PrEmpGroupPO();
        empGroupPO.setId(id);
        return empGroupMapper.deleteById(empGroupPO);
    }


    @Override
    public PrEmpGroupPO getEmployeeGroup(Integer id, String empGroupCode, String empGroupName) {
        if(id > 0)
        {
            return empGroupMapper.selectById(id);
        }
        else
        {
            EntityWrapper<PrEmpGroupPO> entityWrapper = new EntityWrapper<>();

            if(!empGroupCode.isEmpty())
            {
                entityWrapper.where("emp_group_code={0}", empGroupCode);
                return SqlHelper.getObject(empGroupMapper.selectList(entityWrapper));
            }

            else if(!empGroupName.isEmpty()){
                entityWrapper.where("name={0}", empGroupName);
                return SqlHelper.getObject(empGroupMapper.selectList(entityWrapper));
            }
            else{
                return null;
            }
        }
    }

    @Override
    public PageInfo<PrEmpGroupPO> getEmployeeGroups(PrEmpGroupPO empGroupPO, Integer pageNum,Integer pageSize) {

        List<PrEmpGroupPO> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        try {
            resultList = empGroupMapper.getEmployeeGroups(empGroupPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrEmpGroupPO> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public Integer editEmployeeGroup(PrEmpGroupPO prEmpGroupPO) {
        return empGroupMapper.updateById(prEmpGroupPO);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean batchDelete(List<String> ids) {
        try
        {
            empGroupEmpRelationMapper.delByEmpGroupId(ids);
            empGroupMapper.deleteBatchIds(ids);
            if(ids.size() > 0){
                PayrollEmpGroup payrollEmpGroup = new PayrollEmpGroup();
                payrollEmpGroup.setEmpGroupIds(ids);
                payrollEmpGroup.setOperateType(OperateTypeEnum.DELETE.getValue());
                sender.SendEmpGroup(payrollEmpGroup);
            }
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    @Override
    public Integer isExistEmpGroup(String managementId, String empGroupName) {
        return empGroupMapper.isExistEmpGroup(managementId,empGroupName);
    }
}
