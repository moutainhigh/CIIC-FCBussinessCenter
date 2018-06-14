package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantPayrollService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantFinanceBO;
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
    private CommonService commonService;
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    /**
     * 生成财务报表
     * @author chenpb
     * @since 2018-05-25
     * @param taskCode：任务单编号
     * @return SalaryGrantFinanceBO：财务报表
     */
    @Override
    public SalaryGrantFinanceBO createFinanceDetail(String taskCode) {
        SalaryGrantFinanceBO financeBo = BeanUtils.instantiate(SalaryGrantFinanceBO.class);
        FinanceTaskBO task = salaryGrantSubTaskMapper.selectTaskForFinance(taskCode);
        List<FinanceEmployeeBO> empList  = salaryGrantEmployeeMapper.selectEmpForFinance(taskCode);
        if (task!=null && !empList.isEmpty()) {
            List<FinanceEmployeeBO> subTotalList  = new ArrayList<>();
            List<FinanceEmployeeBO> groupList  = new ArrayList<>();
            /** 统计 */
            Long amountNum = empList.parallelStream().filter( x -> BigDecimal.ZERO.compareTo(x.getPaymentAmount())==0).count();
            Long yearBonusNum = empList.parallelStream().filter( x -> BigDecimal.ZERO.compareTo(x.getYearEndBonus())==0).count();
            task.setAmountNum(amountNum.intValue());
            task.setYearBonusNum(yearBonusNum.intValue());
            /** 聚合 */
            Map<String, Map<Integer, List<FinanceEmployeeBO>>> empGroup = empList.parallelStream().collect(Collectors.groupingBy(FinanceEmployeeBO::getCompanyId, Collectors.groupingBy(FinanceEmployeeBO::getTemplateType)));
            empGroup.forEach((key,value)-> value.forEach((innerKey,innerValue)-> { FinanceEmployeeBO subTotal = summaryInfo(0, innerValue); groupList.addAll(innerValue); groupList.add(subTotal); subTotalList.add(subTotal); }));
            groupList.parallelStream().forEach(x -> x.setTemplateName(commonService.getNameByValue("employeeType", String.valueOf(x.getTemplateType()))));
            groupList.add(summaryInfo(1, subTotalList));
            financeBo.setEmpList(groupList);
            task.setOperatorUserId(commonService.getUserNameById(task.getOperatorUserId()));
            task.setApproveUserId(commonService.getUserNameById(task.getApproveUserId()));
        }
        financeBo.setTask(task);
        return financeBo;
    }

    /**
     * @description 统计汇总
     * @author chenpb
     * @since 2018-05-28
     * @param type：统计类型
     * @param list：统计对象
     * @return FinanceEmployeeBO：统计结果
     */
    private FinanceEmployeeBO summaryInfo (Integer type, List<FinanceEmployeeBO> list) {
        FinanceEmployeeBO summary = BeanUtils.instantiate(FinanceEmployeeBO.class);
        if (type==0) {
            summary.setCompanyId("小计");
            summary.setEmployeeName(String.valueOf(list.size()));
            summary.setTotalSum(list.size());
        } else {
            summary.setCompanyId("合计");
            Integer sum = list.parallelStream().mapToInt(FinanceEmployeeBO::getTotalSum).sum();
            summary.setEmployeeName(String.valueOf(sum));
            summary.setTotalSum(sum);
        }
        summary.setWagePayable(list.parallelStream().map(FinanceEmployeeBO::getWagePayable).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.setPersonalSocialSecurity(list.parallelStream().map(FinanceEmployeeBO::getPersonalSocialSecurity).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.setIndividualProvidentFund(list.parallelStream().map(FinanceEmployeeBO::getIndividualProvidentFund).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.setTaxAF(list.parallelStream().map(FinanceEmployeeBO::getTaxAF).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.setTaxFC(list.parallelStream().map(FinanceEmployeeBO::getTaxFC).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.setTaxBPO(list.parallelStream().map(FinanceEmployeeBO::getTaxBPO).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.setTaxIndependence(list.parallelStream().map(FinanceEmployeeBO::getTaxIndependence).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.setPaymentAmount(list.parallelStream().map(FinanceEmployeeBO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        return summary;
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
