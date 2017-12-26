package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeTestPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupEmpRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeTestMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeTestService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houwanhua on 2017/12/8.
 */
@Service
public class EmployeeTestServiceImpl implements EmployeeTestService {

    @Autowired
    private PrEmployeeTestMapper employeeTestMapper;

    @Autowired
    private PrEmpGroupEmpRelationMapper empGroupEmpRelationMapper;


    @Override
    public Integer addEmployeeTest(PrEmployeeTestPO employeeTestPO) {
        return employeeTestMapper.insert(employeeTestPO);
    }

    @Override
    public PageInfo<PrEmployeeTestPO> getEmployees(PrEmployeeTestPO employeeTestPO, Integer pageNum, Integer pageSize) {
        List<PrEmployeeTestPO> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        try {
            resultList = employeeTestMapper.getEmployees(employeeTestPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrEmployeeTestPO> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }
}
