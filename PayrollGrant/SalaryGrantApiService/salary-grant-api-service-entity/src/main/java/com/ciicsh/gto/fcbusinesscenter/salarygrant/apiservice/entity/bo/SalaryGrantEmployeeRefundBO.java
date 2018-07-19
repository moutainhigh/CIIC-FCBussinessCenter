package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 退票发放雇员信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantEmployeeRefundBO implements Serializable {
    /**
     * 任务单编号（发放批次号）
     */
    private String taskCode;
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放
     */
    private Integer grantType;
    /**
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 雇员银行卡账号（原）
     */
    private String cardNum;
    /**
     * 雇员银行卡账号（新）
     */
    private String cardNumNew;
    /**
     * 薪酬计算批次号（新）
     */
    private String batchCodeNew;
}
