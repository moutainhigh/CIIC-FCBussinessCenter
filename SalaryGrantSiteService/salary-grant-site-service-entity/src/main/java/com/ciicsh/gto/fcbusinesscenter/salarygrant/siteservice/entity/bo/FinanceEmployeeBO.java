package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.*;

import java.math.BigDecimal;

/**
 * <p>
 * 财务报表雇员信息
 * </p>
 *
 * @author chenpb
 * @since 2018-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FinanceEmployeeBO {
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 雇员姓名
     */
    private String employeeName;
    /**
     * 个税期间
     */
    private String taxCycle;
    /**
     * 服务大类:0-人事派遣、1-人事代理、2-外包
     */
    private Integer templateType;
    /**
     * 服务大类名
     */
    private String templateName;
    /**
     * 应付工资
     */
    private BigDecimal wagePayable;
    /**
     * 个人社保
     */
    private BigDecimal personalSocialSecurity;
    /**
     * 个人公积金
     */
    private BigDecimal individualProvidentFund;
    /**
     * 申报账户类别
     */
    private Integer declarationAccountCategory;
    /**
     * 扣个人所得税（中智公司）
     */
    private BigDecimal taxAF;
    /**
     * 扣个人所得税（财务公司-FC）
     */
    private BigDecimal taxFC;

    /**
     * 扣个人所得税（外服-BPO）
     */
    private BigDecimal taxBPO;
    /**
     * 扣个人所得税（独立库）
     */
    private BigDecimal taxIndependence;
    /**
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 年终奖
     */
    private BigDecimal yearEndBonus;
    /**
     * 汇总
     */
    private Integer totalSum;

}
