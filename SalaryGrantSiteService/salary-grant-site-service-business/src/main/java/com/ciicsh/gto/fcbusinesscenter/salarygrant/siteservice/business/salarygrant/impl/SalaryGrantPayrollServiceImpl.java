package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantPayrollService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantFinanceBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 生成财务报表
     * @author chenpb
     * @since 2018-05-25
     * @param taskCode
     * @return
     */
    @Override
    public SalaryGrantFinanceBO createFinanceDetail(String taskCode) {
        SalaryGrantFinanceBO bo = new SalaryGrantFinanceBO();
        FinanceTaskBO task = salaryGrantSubTaskMapper.selectTaskForFinance(taskCode);
        List<FinanceEmployeeBO> empList  = salaryGrantEmployeeMapper.selectEmpForFinance(taskCode);
        if (!empList.isEmpty()) {
            task.setTaxCycle(empList.get(0).getTaxCycle());
            List<FinanceEmployeeBO> amountList = empList.stream().filter(x -> Integer.valueOf(0).equals(x.getPaymentAmount())).collect(Collectors.toList());
            List<FinanceEmployeeBO> bonusList = empList.stream().filter(x -> Integer.valueOf(0).equals(x.getYearEndBonus())).collect(Collectors.toList());
            task.setAmountNum(amountList.size());
            task.setYearBonusNum(bonusList.size());
        } else {
            task.setAmountNum(0);
            task.setYearBonusNum(0);
        }
        bo.setTask(task);
        bo.setEmpList(empList);
        return bo;
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
