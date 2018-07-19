package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/25 0025
 */
@ToString(callSuper = true)
@Data
public class SalaryGrantTaskPaymentBO extends SalaryGrantTaskBO {
    /**
     * 任务单主表编号
     */
    private String mainTaskCode;
    /**
     * 正常发放雇员的实发工资之和
     */
    private BigDecimal payAmount;
    /**
     * 正常发放雇员的雇员数量
     */
    private Integer payEmployeeCount;
    /**
     * 申请人
     */
    private String applyer;
    /**
     * 申请日期
     */
    private String applyDate;
    /**
     * 财务代理费
     */
    private BigDecimal agentFeeAmount;
    /**
     * 是否垫付
     */
    private Integer isAdvance;

}
