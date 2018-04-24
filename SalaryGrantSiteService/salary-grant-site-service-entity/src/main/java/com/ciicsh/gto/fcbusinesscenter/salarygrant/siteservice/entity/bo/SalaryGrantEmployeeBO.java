package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放雇员信息表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-02
 */
public class SalaryGrantEmployeeBO  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 雇员信息编号
     */
    private Long salaryGrantEmployeeId;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 薪资发放任务单主表编号
     */
    private String salaryGrantMainTaskCode;
    /**
     * 薪资发放任务单子表编号
     */
    private String salaryGrantSubTaskCode;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 服务大类:0-人事派遣、1-人事代理、2-外包
     */
    private Integer templateType;
    /**
     * 服务大类名称
     */
    private String templateTypeName;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 雇员服务协议编号
     */
    private String employeeServiceAgreementId;
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
     * 发放账户编号
     */
    private String grantAccountCode;
    /**
     * 发放账户名称
     */
    private String grantAccountName;
    /**
     * 发放方式
     */
    private Integer grantMode;
    /**
     * 发放方式名称
     */
    private String grantModeName;
    /**
     * 薪资发放规则编号
     */
    private Long salaryGrantRuleId;
    /**
     * 规则类型:0-固定金额、1-固定比例
     */
    private Integer ruleType;
    /**
     * 规则金额
     */
    private BigDecimal ruleAmount;
    /**
     * 规则比例
     */
    private BigDecimal ruleRatio;
    /**
     * 银行卡编号
     */
    private Long bankcardId;
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
     * 银行卡省份代码
     */
    private String bankcardProvinceCode;
    /**
     * 银行卡城市代码
     */
    private String bankcardCityCode;
    /**
     * 是否默认卡
     */
    private Boolean isDefaultCard;
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
     * 人民币金额
     */
    private BigDecimal paymentAmountRMB;
    /**
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 发放币种
     */
    private String currencyCode;
    /**
     * 发放币种名称
     */
    private String currencyName;
    /**
     * 汇率
     */
    private BigDecimal exchange;
    /**
     * 国籍
     */
    private String countryCode;
    /**
     * 国籍中文
     */
    private String countryName;
    /**
     * 服务周期规则编号
     */
    private Integer cycleRuleId;
    /**
     * 发放金额折合人民币
     */
    private BigDecimal paymentAmountForRMB;
    /**
     * 薪资发放日期
     */
    private String grantDate;
    /**
     * 薪资发放时段:1-上午，2-下午
     */
    private Integer grantTime;
    /**
     * 薪资发放时段中文
     */
    private String grantTimeName;
    /**
     * 发放服务标识:0-薪资发放，1-个税，2-薪资发放+个税
     */
    private Integer grantServiceType;
    /**
     * 合同类型:0-渠道方合同 1-业务合同 2-内部确认单（FC和AF签订）3-合同附件 4-委托合同
     */
    private Integer contractType;
    /**
     * 业务合同编号
     */
    private String contractId;
    /**
     * 合同我方
     */
    private String contractFirstParty;
    /**
     * 付社保/公积金标识:0-否，1-是
     */
    private Boolean isWelfareIncluded;
    /**
     * 薪酬服务费
     */
    private BigDecimal serviceFeeAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 变更日志
     */
    private String changeLog;
    /**
     * 调整信息
     */
    private String adjustCompareInfo;
    /**
     * 发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
     */
    private Integer grantStatus;
    /**
     * 发放状态中文
     */
    private String grantStatusName;
    /**
     * 暂缓类型:0-全部暂缓，1-部分暂缓
     */
    private Integer reprieveType;
    /**
     * 暂缓类型名称
     */
    private String reprieveTypeName;

    public Long getSalaryGrantEmployeeId() {
        return salaryGrantEmployeeId;
    }

    public void setSalaryGrantEmployeeId(Long salaryGrantEmployeeId) {
        this.salaryGrantEmployeeId = salaryGrantEmployeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSalaryGrantMainTaskCode() {
        return salaryGrantMainTaskCode;
    }

    public void setSalaryGrantMainTaskCode(String salaryGrantMainTaskCode) {
        this.salaryGrantMainTaskCode = salaryGrantMainTaskCode;
    }

    public String getSalaryGrantSubTaskCode() {
        return salaryGrantSubTaskCode;
    }

    public void setSalaryGrantSubTaskCode(String salaryGrantSubTaskCode) {
        this.salaryGrantSubTaskCode = salaryGrantSubTaskCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
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

    public String getGrantAccountCode() {
        return grantAccountCode;
    }

    public void setGrantAccountCode(String grantAccountCode) {
        this.grantAccountCode = grantAccountCode;
    }

    public Integer getGrantMode() {
        return grantMode;
    }

    public void setGrantMode(Integer grantMode) {
        this.grantMode = grantMode;
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

    public void setGrantStatus(Integer grantStatus) {
        this.grantStatus = grantStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public BigDecimal getPaymentAmountForRMB() {
        return paymentAmountForRMB;
    }

    public void setPaymentAmountForRMB(BigDecimal paymentAmountForRMB) {
        this.paymentAmountForRMB = paymentAmountForRMB;
    }

    public Long getSalaryGrantRuleId() {
        return salaryGrantRuleId;
    }

    public void setSalaryGrantRuleId(Long salaryGrantRuleId) {
        this.salaryGrantRuleId = salaryGrantRuleId;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
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

    public Long getBankcardId() {
        return bankcardId;
    }

    public void setBankcardId(Long bankcardId) {
        this.bankcardId = bankcardId;
    }

    public Boolean getDefaultCard() {
        return isDefaultCard;
    }

    public void setDefaultCard(Boolean defaultCard) {
        isDefaultCard = defaultCard;
    }

    public BigDecimal getPaymentAmountRMB() {
        return paymentAmountRMB;
    }

    public void setPaymentAmountRMB(BigDecimal paymentAmountRMB) {
        this.paymentAmountRMB = paymentAmountRMB;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getEmployeeServiceAgreementId() {
        return employeeServiceAgreementId;
    }

    public void setEmployeeServiceAgreementId(String employeeServiceAgreementId) {
        this.employeeServiceAgreementId = employeeServiceAgreementId;
    }

    public Integer getCycleRuleId() {
        return cycleRuleId;
    }

    public void setCycleRuleId(Integer cycleRuleId) {
        this.cycleRuleId = cycleRuleId;
    }

    public String getAdjustCompareInfo() {
        return adjustCompareInfo;
    }

    public void setAdjustCompareInfo(String adjustCompareInfo) {
        this.adjustCompareInfo = adjustCompareInfo;
    }

    public Integer getReprieveType() {
        return reprieveType;
    }

    public void setReprieveType(Integer reprieveType) {
        this.reprieveType = reprieveType;
    }

    public String getTemplateTypeName() {
        return templateTypeName;
    }

    public void setTemplateTypeName(String templateTypeName) {
        this.templateTypeName = templateTypeName;
    }

    public String getGrantAccountName() {
        return grantAccountName;
    }

    public void setGrantAccountName(String grantAccountName) {
        this.grantAccountName = grantAccountName;
    }

    public String getGrantModeName() {
        return grantModeName;
    }

    public void setGrantModeName(String grantModeName) {
        this.grantModeName = grantModeName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getGrantTimeName() {
        return grantTimeName;
    }

    public void setGrantTimeName(String grantTimeName) {
        this.grantTimeName = grantTimeName;
    }

    public String getGrantStatusName() {
        return grantStatusName;
    }

    public void setGrantStatusName(String grantStatusName) {
        this.grantStatusName = grantStatusName;
    }

    public String getReprieveTypeName() {
        return reprieveTypeName;
    }

    public void setReprieveTypeName(String reprieveTypeName) {
        this.reprieveTypeName = reprieveTypeName;
    }

    public Integer getGrantServiceType() {
        return grantServiceType;
    }

    public void setGrantServiceType(Integer grantServiceType) {
        this.grantServiceType = grantServiceType;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractFirstParty() {
        return contractFirstParty;
    }

    public void setContractFirstParty(String contractFirstParty) {
        this.contractFirstParty = contractFirstParty;
    }

    public Boolean getWelfareIncluded() {
        return isWelfareIncluded;
    }

    public void setWelfareIncluded(Boolean welfareIncluded) {
        isWelfareIncluded = welfareIncluded;
    }

    public String getBankcardProvinceCode() {
        return bankcardProvinceCode;
    }

    public void setBankcardProvinceCode(String bankcardProvinceCode) {
        this.bankcardProvinceCode = bankcardProvinceCode;
    }

    public String getBankcardCityCode() {
        return bankcardCityCode;
    }

    public void setBankcardCityCode(String bankcardCityCode) {
        this.bankcardCityCode = bankcardCityCode;
    }

    public BigDecimal getServiceFeeAmount() {
        return serviceFeeAmount;
    }

    public void setServiceFeeAmount(BigDecimal serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }
}
