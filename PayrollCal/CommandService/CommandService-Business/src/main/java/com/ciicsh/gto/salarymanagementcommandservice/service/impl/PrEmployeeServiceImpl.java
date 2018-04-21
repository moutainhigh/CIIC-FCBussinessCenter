package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.IPrEmployeeMapper;
import com.ciicsh.gto.salarymanagement.entity.PrEmployeeEntity;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/31.
 */
@Service
public class PrEmployeeServiceImpl implements com.ciicsh.gto.salarymanagementcommandservice.service.PrEmployeeService {

    @Autowired
    private IPrEmployeeMapper prEmployeeMapper;

    @Autowired
    private PrEmployeeMapper employeeMapper;

    final static int PAGE_SIZE = 10;

    @Override
    public PageInfo<PrEmployeeEntity> getEmployeeList(PrEmployeeEntity param, Integer pageNum) {

        List<PrEmployeeEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prEmployeeMapper.selectList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrEmployeeEntity> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public List<PrEmployeePO> getEmployeesByGroupCode(String empGroupCode) {
        return employeeMapper.getEmployeesByGroupCode(empGroupCode);
    }

}
