package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 雇员服务协议
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-01
 */
public class EmployeeServiceAgreementDTO extends PagingDTO implements Serializable {
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
     * 旧值--发放服务标识：0-薪资发放，1-个税，2-薪资发放 + 个税
     * 新值--服务类别 ( 0 - 工资和税, 1 - 仅个税, 2 - 净工资, 3 - 仅计算 )serviceCategory
     */
    private Integer grantServiceType;
    /**
     * 薪资发放规则Id
     */
    private List<Integer> salaryGrantRuleId;
    /**
     * 汇率计算方式:0 - 固定，1 - 实时，2-记账
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
     * 供应商名称
     */
    private String supplierName;
    /**
     * 供应商收款账户
     */
    private String supplierAccountReceivale;
    /**
     * 供应商收款账户名称
     */
    private String supplierAccountReceivaleName;
    /**
     * 业务合同编号（Entity Id）- HT+分类+YY+5位数字
     */
    private String contractId;
    /**
     * 合同类型 (0-渠道方合同 1-业务合同 2-内部确认单（FC和AF签订）3-合同附件 4-委托合同)
     */
    private Integer contractType;
    /**
     * 合同我方（1-AF/2-FC/3-BPO）
     */
    private String contractFirstParty;
    /**
     * 付款账号
     */
    private String paymentBankAccount;
    /**
     * 付款账户名
     */
    private String paymentBankAccountName;
    /**
     * 付款银行名
     */
    private String paymentBankName;
    /**
     * 个税期间 (0-当月 1-下月 2-下下月)
     */
    private Integer taxPeriod;
    /**
     * 申报账户
     */
    private String declarationAccount;
    /**
     * 申报账户类别 (1-大库（FC目前服务协议只配置FC大库），2-独立库)
     * declarationAccountDetail--source
     */
    private Integer declarationAccountCategory;
    /**
     * 缴纳账户
     */
    private String contributionAccount;
    /**
     * 缴纳账户类别 (1-大库（FC目前服务协议只配置FC大库），2-独立库)
     * contributionAccountDetail--source
     */
    private Integer contributionAccountCategory;
    /**
     * 委托合同流水号
     */
    private Long commissionContractSerialNumber;
    /**
     * 社保数据来源 ( 0 - AF, 1 - 客户 )
     */
    private Integer socialSecuritySource;
    /**
     * 个人养老 ( 0 - 工资内含, 1 - 工资不含 )
     */
    private Integer personalPension;
    /**
     * 个人医疗 ( 0 - 工资内含, 1 - 工资不含 )
     */
    private Integer personalMedicalTreatment;
    /**
     * 个人失业( 0 - 工资内含, 1 - 工资不含 )
     */
    private Integer individualUnemployment;
    /**
     * 个人公积金( 0 - 工资内含, 1 - 工资不含 )
     */
    private Integer individualProvidentFund;


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

    public List<Integer> getSalaryGrantRuleId() {
        return salaryGrantRuleId;
    }

    public void setSalaryGrantRuleId(List<Integer> salaryGrantRuleId) {
        this.salaryGrantRuleId = salaryGrantRuleId;
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

    public String getSupplierAccountReceivaleName() {
        return supplierAccountReceivaleName;
    }

    public void setSupplierAccountReceivaleName(String supplierAccountReceivaleName) {
        this.supplierAccountReceivaleName = supplierAccountReceivaleName;
    }

    public String getPaymentBankAccount() {
        return paymentBankAccount;
    }

    public void setPaymentBankAccount(String paymentBankAccount) {
        this.paymentBankAccount = paymentBankAccount;
    }

    public String getPaymentBankAccountName() {
        return paymentBankAccountName;
    }

    public void setPaymentBankAccountName(String paymentBankAccountName) {
        this.paymentBankAccountName = paymentBankAccountName;
    }

    public String getPaymentBankName() {
        return paymentBankName;
    }

    public void setPaymentBankName(String paymentBankName) {
        this.paymentBankName = paymentBankName;
    }

    public Integer getTaxPeriod() {
        return taxPeriod;
    }

    public void setTaxPeriod(Integer taxPeriod) {
        this.taxPeriod = taxPeriod;
    }

    public String getDeclarationAccount() {
        return declarationAccount;
    }

    public void setDeclarationAccount(String declarationAccount) {
        this.declarationAccount = declarationAccount;
    }

    public Integer getDeclarationAccountCategory() {
        return declarationAccountCategory;
    }

    public void setDeclarationAccountCategory(Integer declarationAccountCategory) {
        this.declarationAccountCategory = declarationAccountCategory;
    }

    public String getContributionAccount() {
        return contributionAccount;
    }

    public void setContributionAccount(String contributionAccount) {
        this.contributionAccount = contributionAccount;
    }

    public Integer getContributionAccountCategory() {
        return contributionAccountCategory;
    }

    public void setContributionAccountCategory(Integer contributionAccountCategory) {
        this.contributionAccountCategory = contributionAccountCategory;
    }

    public Long getCommissionContractSerialNumber() {
        return commissionContractSerialNumber;
    }

    public void setCommissionContractSerialNumber(Long commissionContractSerialNumber) {
        this.commissionContractSerialNumber = commissionContractSerialNumber;
    }

    public Integer getSocialSecuritySource() {
        return socialSecuritySource;
    }

    public void setSocialSecuritySource(Integer socialSecuritySource) {
        this.socialSecuritySource = socialSecuritySource;
    }

    public Integer getPersonalPension() {
        return personalPension;
    }

    public void setPersonalPension(Integer personalPension) {
        this.personalPension = personalPension;
    }

    public Integer getPersonalMedicalTreatment() {
        return personalMedicalTreatment;
    }

    public void setPersonalMedicalTreatment(Integer personalMedicalTreatment) {
        this.personalMedicalTreatment = personalMedicalTreatment;
    }

    public Integer getIndividualUnemployment() {
        return individualUnemployment;
    }

    public void setIndividualUnemployment(Integer individualUnemployment) {
        this.individualUnemployment = individualUnemployment;
    }

    public Integer getIndividualProvidentFund() {
        return individualProvidentFund;
    }

    public void setIndividualProvidentFund(Integer individualProvidentFund) {
        this.individualProvidentFund = individualProvidentFund;
    }
}
