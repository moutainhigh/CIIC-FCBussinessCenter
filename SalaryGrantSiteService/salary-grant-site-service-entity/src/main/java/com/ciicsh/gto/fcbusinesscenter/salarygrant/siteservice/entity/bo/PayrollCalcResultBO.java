package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资计算批次结果表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-01
 */
public class PayrollCalcResultBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 雇员中智终身编号
     */
    private String employeeId;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 雇佣性质（当前公司）：0-派遣，1-代理，2-外包
     */
    private Integer templateType;
    /**
     * 服务周期规则Id
     */
    private Integer cycleRuleId;
    /**
     * 发放方式
     */
    private Integer grantType;
    /**
     * 发放账户编号
     */
    private String grantAccountCode;
    /**
     * 发放服务标识：0-薪资发放，1-个税，2-薪资发放 + 个税
     */
    private Integer grantServiceType;
    /**
     * 薪资发放规则Id
     */
    private Long empSalaryGrantRuleId;
    /**
     * 汇率计算方式（0-固定， 1-实时）
     */
    private Integer exchangeCalcMode;
    /**
     * 客户约定汇率
     */
    private BigDecimal customerAgreedExchangeRate;
    /**
     * 是否供应商
     */
    private Boolean isSupplier;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 委托协议编号
     */
    private String agencyAgreementCode;

    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 个税期间
     */
    private String taxCycle;
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
     * 个人所得税
     */
    private BigDecimal personalIncomeTax;
    /**
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 发放币种
     */
    private String currencyCode;
    /**
     * 币种名称
     */
    private String currency;
    /**
     * 汇率
     */
    private BigDecimal exchange;
    /**
     * 国籍
     */
    private String countryCode;
    /**
     * 发放状态
     */
    private Integer grantStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 薪资发放日期
     */
    private String grantDate;
    /**
     * 薪资发放时段:1-上午，2-下午
     */
    private Integer grantTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public Integer getCycleRuleId() {
        return cycleRuleId;
    }

    public void setCycleRuleId(Integer cycleRuleId) {
        this.cycleRuleId = cycleRuleId;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public String getGrantAccountCode() {
        return grantAccountCode;
    }

    public void setGrantAccountCode(String grantAccountCode) {
        this.grantAccountCode = grantAccountCode;
    }

    public Integer getGrantServiceType() {
        return grantServiceType;
    }

    public void setGrantServiceType(Integer grantServiceType) {
        this.grantServiceType = grantServiceType;
    }

    public Long getEmpSalaryGrantRuleId() {
        return empSalaryGrantRuleId;
    }

    public void setEmpSalaryGrantRuleId(Long empSalaryGrantRuleId) {
        this.empSalaryGrantRuleId = empSalaryGrantRuleId;
    }

    public Integer getExchangeCalcMode() {
        return exchangeCalcMode;
    }

    public void setExchangeCalcMode(Integer exchangeCalcMode) {
        this.exchangeCalcMode = exchangeCalcMode;
    }

    public Boolean getIsSupplier() {
        return isSupplier;
    }

    public void setIsSupplier(Boolean isSupplier) {
        this.isSupplier = isSupplier;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAgencyAgreementCode() {
        return agencyAgreementCode;
    }

    public void setAgencyAgreementCode(String agencyAgreementCode) {
        this.agencyAgreementCode = agencyAgreementCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getGrantCycle() {
        return grantCycle;
    }

    public void setGrantCycle(String grantCycle) {
        this.grantCycle = grantCycle;
    }

    public String getTaxCycle() {
        return taxCycle;
    }

    public void setTaxCycle(String taxCycle) {
        this.taxCycle = taxCycle;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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

    public BigDecimal getWagePayable() {
        return wagePayable;
    }

    public void setWagePayable(BigDecimal wagePayable) {
        this.wagePayable = wagePayable;
    }

    public BigDecimal getPersonalSocialSecurity() {
        return personalSocialSecurity;
    }

    public void setPersonalSocialSecurity(BigDecimal personalSocialSecurity) {
        this.personalSocialSecurity = personalSocialSecurity;
    }

    public BigDecimal getIndividualProvidentFund() {
        return individualProvidentFund;
    }

    public void setIndividualProvidentFund(BigDecimal individualProvidentFund) {
        this.individualProvidentFund = individualProvidentFund;
    }

    public BigDecimal getPersonalIncomeTax() {
        return personalIncomeTax;
    }

    public void setPersonalIncomeTax(BigDecimal personalIncomeTax) {
        this.personalIncomeTax = personalIncomeTax;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getGrantStatus() {
        return grantStatus;
    }

    public void settGrantStatus(Integer grantStatus) {
        this.grantStatus = grantStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getCustomerAgreedExchangeRate() {
        return customerAgreedExchangeRate;
    }

    public void setCustomerAgreedExchangeRate(BigDecimal customerAgreedExchangeRate) {
        this.customerAgreedExchangeRate = customerAgreedExchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(String grantDate) {
        this.grantDate = grantDate;
    }

    public Integer getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(Integer grantTime) {
        this.grantTime = grantTime;
    }
}
