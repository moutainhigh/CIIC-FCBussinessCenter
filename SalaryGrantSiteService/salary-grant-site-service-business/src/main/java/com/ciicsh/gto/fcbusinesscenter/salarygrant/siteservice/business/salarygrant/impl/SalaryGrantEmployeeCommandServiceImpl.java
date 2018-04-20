package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 薪资发放雇员信息维护 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
@Service
public class SalaryGrantEmployeeCommandServiceImpl implements SalaryGrantEmployeeCommandService {

    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Boolean toReprieveEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO) {
        // todo
        return null;
    }

    @Override
    public Boolean toRecoverEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO) {
        // todo
        return null;
    }

    @Override
    public Boolean toBatchReprieveEmployee(List<SalaryGrantEmployeePO> salaryGrantEmployeePOList) {
        // todo
        return null;
    }

    @Override
    public Boolean toBatchRecoverEmployee(List<SalaryGrantEmployeePO> salaryGrantEmployeePOList) {
        // todo
        return null;
    }

    @Override
    public void toExportEmployee(String salaryGrantMainTaskCode) {
        // todo
    }
}
