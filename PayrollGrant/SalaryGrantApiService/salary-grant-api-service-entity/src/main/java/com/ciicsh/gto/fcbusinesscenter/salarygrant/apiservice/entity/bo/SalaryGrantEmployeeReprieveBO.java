package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 暂缓发放雇员信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantEmployeeReprieveBO implements Serializable {
    /**
     * 任务单编号
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
     * 收款人账号（原）
     */
    private String cardNum;
    /**
     * 收款人姓名（原）
     */
    private String accountName;
    /**
     * 收款行行号（原）
     */
    private String bankCode;
    /**
     * 收款行名称（原）
     */
    private String depositBank;
    /**
     * 人民币金额
     */
    private BigDecimal paymentAmountRMB;
    /**
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 发放币种:CNY-人民币，USD-美元，EUR-欧元
     */
    private String currencyCode;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
     */
    private Integer grantMode;
    /**
     * 发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
     */
    private Integer grantStatus;
    /**
     * 暂缓类型:0-全部暂缓，1-部分暂缓
     */
    private Integer reprieveType;
    /**
     * 国籍
     */
    private String countryCode;
    /**
     * 雇员银行卡账号（新）
     */
    private String cardNumNew;
    /**
     * 银行卡编号（新）
     */
    private Long bankcardId;
    /**
     * 薪酬计算批次号（新）
     */
    private String batchCodeNew;
}
