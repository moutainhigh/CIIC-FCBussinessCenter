package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupEmpRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by houwanhua on 2017/12/8.
 */
@Service
public class EmployeeTestServiceImpl implements EmployeeTestService {

//    @Autowired
//    private PrEmployeeTestMapper employeeTestMapper;

    @Autowired
    private PrEmpGroupEmpRelationMapper empGroupEmpRelationMapper;


//    @Override
//    public Integer addEmployeeTest(PrEmployeePO employeeTestPO) {
//        return employeeTestMapper.insert(employeeTestPO);
//    }
//
//    @Override
//    public PageInfo<PrEmployeePO> getEmployees(PrEmployeePO employeeTestPO, Integer pageNum, Integer pageSize) {
//        List<PrEmployeePO> resultList = new ArrayList<>();
//        PageHelper.startPage(pageNum, pageSize);
//        try {
//            resultList = employeeTestMapper.getEmployees(employeeTestPO);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        PageInfo<PrEmployeePO> pageInfo = new PageInfo<>(resultList);
//        return  pageInfo;
//    }
}
