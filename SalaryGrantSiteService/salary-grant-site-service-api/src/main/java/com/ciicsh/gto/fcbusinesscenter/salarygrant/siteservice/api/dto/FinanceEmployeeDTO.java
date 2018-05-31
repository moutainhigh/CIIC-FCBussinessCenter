package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 财务报表雇员信息
 * </p>
 *
 * @author chenpb
 * @since 2018-05-25
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class FinanceEmployeeDTO implements Serializable {
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 个税期间
     */
    private String taxCycle;
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
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 年终奖
     */
    private BigDecimal yearEndBonus;
}
