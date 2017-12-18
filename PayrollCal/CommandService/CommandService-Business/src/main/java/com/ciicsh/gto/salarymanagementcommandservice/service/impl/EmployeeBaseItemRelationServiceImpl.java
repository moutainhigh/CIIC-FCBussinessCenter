package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeBaseItemRelationPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeBaseItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeBaseItemRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by houwanhua on 2017/12/11.
 */
@Service
public class EmployeeBaseItemRelationServiceImpl implements EmployeeBaseItemRelationService {

    @Autowired
    private PrEmployeeBaseItemRelationMapper employeeBaseItemRelationMapper;

    @Override
    public Integer addEmployeeBaseItemRelation(PrEmployeeBaseItemRelationPO employeeBaseItemRelationPO) {
        return employeeBaseItemRelationMapper.insert(employeeBaseItemRelationPO);
    }
}
