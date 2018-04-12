package com.ciicsh.gto.fcbusinesscenter.site.service.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 雇员服务协议
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-01
 */
public class EmployeeServiceAgreementDTO extends CommonListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 雇员服务协议编号
     */
    private String employeeServiceAgreementId;
    /**
     * 雇员中智终身编号
     */
    private String employeeId;
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
     * 发放账户
     */
    private String grantAccountCode;
    /**
     * 发放服务标识：0-薪资发放，1-个税，2-薪资发放 + 个税
     */
    private Integer grantServiceType;
    /**
     * 薪资发放规则Id
     */
    private Long salaryGrantRuleId;
    /**
     * 汇率计算方式:0 - 固定， 1 - 实时
     */
    private Integer exchangeCalcMode;
    /**
     * 是否包括社保和公积金
     */
    private Boolean isWelfareIncluded;
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 雇员银行卡号
     */
    private String cardNum;
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
     * 供应商收款账户
     */
    private String supplierAccountReceivale;
    /**
     * 业务合同编号（Entity Id）- HT+分类+YY+5位数字
     */
    private String contractId;
    /**
     * 合同类型 (0-渠道方合同 1-业务合同 2-内部确认单（FC和AF签订）3-合同附件 4-委托合同)
     */
    private Integer contractType;
    /**
     * 合同我方（AF/FC/BPO）
     */
    private String contractFirstParty;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmployeeServiceAgreementId() {
        return employeeServiceAgreementId;
    }

    public void setEmployeeServiceAgreementId(String employeeServiceAgreementId) {
        this.employeeServiceAgreementId = employeeServiceAgreementId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getExchangeCalcMode() {
        return exchangeCalcMode;
    }

    public void setExchangeCalcMode(Integer exchangeCalcMode) {
        this.exchangeCalcMode = exchangeCalcMode;
    }

    public BigDecimal getCustomerAgreedExchangeRate() {
        return customerAgreedExchangeRate;
    }

    public void setCustomerAgreedExchangeRate(BigDecimal customerAgreedExchangeRate) {
        this.customerAgreedExchangeRate = customerAgreedExchangeRate;
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

    public Long getSalaryGrantRuleId() {
        return salaryGrantRuleId;
    }

    public void setSalaryGrantRuleId(Long salaryGrantRuleId) {
        this.salaryGrantRuleId = salaryGrantRuleId;
    }

    public Boolean getSupplier() {
        return isSupplier;
    }

    public void setSupplier(Boolean supplier) {
        isSupplier = supplier;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Boolean getWelfareIncluded() {
        return isWelfareIncluded;
    }

    public void setWelfareIncluded(Boolean welfareIncluded) {
        isWelfareIncluded = welfareIncluded;
    }

    public String getSupplierAccountReceivale() {
        return supplierAccountReceivale;
    }

    public void setSupplierAccountReceivale(String supplierAccountReceivale) {
        this.supplierAccountReceivale = supplierAccountReceivale;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getContractFirstParty() {
        return contractFirstParty;
    }

    public void setContractFirstParty(String contractFirstParty) {
        this.contractFirstParty = contractFirstParty;
    }
}
