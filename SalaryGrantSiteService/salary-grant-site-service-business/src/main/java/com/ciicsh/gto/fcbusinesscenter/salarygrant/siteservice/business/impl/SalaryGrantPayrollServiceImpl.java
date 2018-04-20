package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.SalaryGrantPayrollService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
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
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Boolean toCreatePayrollForBusiness(String salaryGrantMainTaskCode) {
        // todo
        return null;
    }

    @Override
    public Boolean toCreatePayrollForFinance(String salaryGrantMainTaskCode) {
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
