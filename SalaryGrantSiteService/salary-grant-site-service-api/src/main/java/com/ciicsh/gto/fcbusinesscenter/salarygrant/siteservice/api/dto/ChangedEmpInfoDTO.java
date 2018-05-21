package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 变更雇员信息
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChangedEmpInfoDTO {
    /**
     * 雇员信息编号
     */
    private Long salaryGrantEmployeeId;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 收款人账号
     */
    private String cardNum;
    /**
     * 收款人姓名
     */
    private String accountName;
    /**
     * 收款行行号
     */
    private String bankCode;
    /**
     * 收款行名称
     */
    private String depositBank;
    /**
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 发放币种:CNY-人民币，USD-美元，EUR-欧元
     */
    private String currencyCode;
    /**
     * 发放币种名称
     */
    private String currencyName;
    /**
     * 国籍
     */
    private String countryCode;
    /**
     * 国籍中文
     */
    private String countryName;
    /**
     * 备注
     */
    private String remark;
}
