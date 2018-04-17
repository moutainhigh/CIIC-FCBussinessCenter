package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放规则
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-01
 */
public class SalaryGrantRuleDTO extends CommonListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 薪资发放规则Id
     */
    private Long salaryGrantRuleId;
    /**
     * 雇员中智终身编号
     */
    private String employeeId;
    /**
     * 规则类型（固定金额、固定比例）
     */
    private Integer ruleType;
    /**
     * 货币编号
     */
    private String currencyCode;
    /**
     * 币种
     */
    private String currency;
    /**
     * 金额
     */
    private BigDecimal ruleAmount;
    /**
     * 比例
     */
    private BigDecimal ruleRatio;
    /**
     * 雇员银行卡号
     */
    private String cardNum;
    /**
     * 银行卡编号
     */
    private Long bankcardId;

    public Long getSalaryGrantRuleId() {
        return salaryGrantRuleId;
    }

    public void setSalaryGrantRuleId(Long salaryGrantRuleId) {
        this.salaryGrantRuleId = salaryGrantRuleId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRuleAmount() {
        return ruleAmount;
    }

    public void setRuleAmount(BigDecimal ruleAmount) {
        this.ruleAmount = ruleAmount;
    }

    public BigDecimal getRuleRatio() {
        return ruleRatio;
    }

    public void setRuleRatio(BigDecimal ruleRatio) {
        this.ruleRatio = ruleRatio;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Long getBankcardId() {
        return bankcardId;
    }

    public void setBankcardId(Long bankcardId) {
        this.bankcardId = bankcardId;
    }
}
