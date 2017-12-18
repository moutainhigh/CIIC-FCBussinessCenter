package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupEmpRelationPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupEmpRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmpGroupEmpRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/11.
 */
@Service
public class EmpGroupEmpRelationServiceImpl implements EmpGroupEmpRelationService{

    @Autowired
    private PrEmpGroupEmpRelationMapper empGroupEmpRelationMapper;

    @Override
    public Integer addEmpGroupEmpRelation(PrEmpGroupEmpRelationPO empGroupEmpRelationPO) {
        return empGroupEmpRelationMapper.insert(empGroupEmpRelationPO);
    }

    @Override
    public Integer isExistEmpGroupEmpRelation(String empGroupId, String empId) {
        return empGroupEmpRelationMapper.isExistEmpGroupEmpRelation(empGroupId,empId);
    }
}
