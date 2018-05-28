package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.alibaba.fastjson.JSON;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantPayrollService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantFinanceBO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    CommonService commonService;

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
        SalaryGrantFinanceBO financeBo = BeanUtils.instantiate(SalaryGrantFinanceBO.class);
        FinanceTaskBO task = salaryGrantSubTaskMapper.selectTaskForFinance(taskCode);
        List<FinanceEmployeeBO> empList  = salaryGrantEmployeeMapper.selectEmpForFinance(taskCode);
        if (!empList.isEmpty()) {
            List<FinanceEmployeeBO> groupList  = new ArrayList<>();
            task.setTaxCycle(empList.get(0).getTaxCycle());
            /** 统计 */
            Long amountNum = empList.parallelStream().filter( x -> BigDecimal.ZERO.compareTo(x.getPaymentAmount())==0).count();
            Long yearBonusNum = empList.parallelStream().filter( x -> BigDecimal.ZERO.compareTo(x.getYearEndBonus())==0).count();
            task.setAmountNum(amountNum.intValue());
            task.setYearBonusNum(yearBonusNum.intValue());
            /** 聚合 */
            Map<String, Map<Integer, List<FinanceEmployeeBO>>> empGroup = empList.parallelStream().collect(Collectors.groupingBy(FinanceEmployeeBO::getCompanyId, Collectors.groupingBy(FinanceEmployeeBO::getTemplateType)));
            empGroup.forEach((key,value)-> value.forEach((innerKey,innerValue)-> {
                FinanceEmployeeBO summary = BeanUtils.instantiate(FinanceEmployeeBO.class);
                summary.setCompanyId("小计");
                summary.setEmployeeName(String.valueOf(innerValue.size()));
                summary.setWagePayable(innerValue.parallelStream().map(FinanceEmployeeBO::getWagePayable).reduce(BigDecimal.ZERO, BigDecimal::add));
                summary.setPersonalSocialSecurity(innerValue.parallelStream().map(FinanceEmployeeBO::getPersonalSocialSecurity).reduce(BigDecimal.ZERO, BigDecimal::add));
                summary.setIndividualProvidentFund(innerValue.parallelStream().map(FinanceEmployeeBO::getIndividualProvidentFund).reduce(BigDecimal.ZERO, BigDecimal::add));
                summary.setPaymentAmount(innerValue.parallelStream().map(FinanceEmployeeBO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                groupList.addAll(innerValue);
                groupList.add(summary);
            }));
            groupList.parallelStream().forEach(x -> x.setTemplateName(commonService.getNameByValue("employeeType", String.valueOf(x.getTemplateType()))));
            financeBo.setEmpList(groupList);
        } else {
            task.setAmountNum(0);
            task.setYearBonusNum(0);
        }
        financeBo.setTask(task);
        return financeBo;
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
