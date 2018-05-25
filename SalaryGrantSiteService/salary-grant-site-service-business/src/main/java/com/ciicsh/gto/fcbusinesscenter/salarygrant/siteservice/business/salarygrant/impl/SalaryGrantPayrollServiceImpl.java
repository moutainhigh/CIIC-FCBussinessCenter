package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantPayrollService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 薪资发放工资清单 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
@Service
public class SalaryGrantPayrollServiceImpl implements SalaryGrantPayrollService {
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;

    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Boolean toCreatePayrollForFinance(String taskCode) {
        SalaryGrantTaskBO financeTask = salaryGrantSubTaskMapper.selectTaskForFinance(taskCode);
        List<FinanceEmployeeBO> empList  = salaryGrantEmployeeMapper.selectEmpForFinance(taskCode);
        return true;
    }

    @Override
    public Boolean toCreatePayrollForBusiness(String salaryGrantMainTaskCode) {
        // todo
        return null;
    }

    @Override
    public void toPreviewForPayroll(List reviewInfo) {
        // todo
    }

    @Override
    public void toPrintForPayroll(List printInfo) {
        // todo
    }

}
