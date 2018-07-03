package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 雇员银行卡信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-01
 */
public class EmployeeBankcardDTO extends PagingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 银行卡编号
     */
    private Long bankcardId;
    /**
     * 雇员中智终身编号
     */
    private String employeeId;
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
     * 银行国际代码
     */
    private String swiftCode;
    /**
     * 国际银行账户号码
     */
    private String iban;
    /**
     * 银行卡种类
     */
    private Integer bankcardType;
    /**
     * 银行卡用途：1:工资卡 (AF - 2:报销卡 3:工资报销卡)
     */
    private Integer usage;
    /**
     * 默认银行卡
     */
    private Boolean isDefaultCard;
    /**
     * 默认卡币种
     */
    private String defaultCardCurrencyCode;
    /**
     * 默认卡汇率
     */
    private BigDecimal defaultCardExchange;
    /**
     * 省份代码
     */
    private String provinceCode;
    /**
     * 城市代码
     */
    private String cityCode;

    public Long getBankcardId() {
        return bankcardId;
    }

    public void setBankcardId(Long bankcardId) {
        this.bankcardId = bankcardId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Integer getBankcardType() {
        return bankcardType;
    }

    public void setBankcardType(Integer bankcardType) {
        this.bankcardType = bankcardType;
    }

    public Boolean getDefaultCard() {
        return isDefaultCard;
    }

    public void setDefaultCard(Boolean defaultCard) {
        isDefaultCard = defaultCard;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public String getDefaultCardCurrencyCode() {
        return defaultCardCurrencyCode;
    }

    public void setDefaultCardCurrencyCode(String defaultCardCurrencyCode) {
        this.defaultCardCurrencyCode = defaultCardCurrencyCode;
    }

    public BigDecimal getDefaultCardExchange() {
        return defaultCardExchange;
    }

    public void setDefaultCardExchange(BigDecimal defaultCardExchange) {
        this.defaultCardExchange = defaultCardExchange;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
