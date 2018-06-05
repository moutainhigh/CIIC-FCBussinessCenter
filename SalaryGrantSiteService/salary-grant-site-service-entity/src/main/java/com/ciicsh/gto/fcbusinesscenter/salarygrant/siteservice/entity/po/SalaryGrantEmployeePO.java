package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 薪资发放雇员信息表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@TableName("sg_salary_grant_employee")
public class SalaryGrantEmployeePO extends Model<SalaryGrantEmployeePO> implements Cloneable{

    private static final long serialVersionUID = 1L;

    /**
     * 雇员信息编号
     */
	@TableId(value="salary_grant_employee_id", type= IdType.AUTO)
	private Long salaryGrantEmployeeId;
    /**
     * 雇员编号
     */
	@TableField("employee_id")
	private String employeeId;
    /**
     * 薪资发放任务单主表编号
     */
	@TableField("salary_grant_main_task_code")
	private String salaryGrantMainTaskCode;
    /**
     * 薪资发放任务单子表编号
     */
	@TableField("salary_grant_sub_task_code")
	private String salaryGrantSubTaskCode;
    /**
     * 雇员名称
     */
	@TableField("employee_name")
	private String employeeName;
    /**
     * 服务大类:1:派遣 2:代理 3:外包
     */
	@TableField("template_type")
	private Integer templateType;
    /**
     * 公司编号
     */
	@TableField("company_id")
	private String companyId;
    /**
     * 公司名称
     */
	@TableField("company_name")
	private String companyName;
    /**
     * 雇员服务协议编号
     */
    @TableField("employee_service_agreement_id")
    private String employeeServiceAgreementId;
    /**
     * 薪资周期
     */
	@TableField("grant_cycle")
	private String grantCycle;
    /**
     * 个税期间
     */
	@TableField("tax_cycle")
	private String taxCycle;
    /**
     * 薪酬计算批次号
     */
	@TableField("batch_code")
	private String batchCode;
    /**
     * 发放账户编号
     */
	@TableField("grant_account_code")
	private String grantAccountCode;
	/**
	 * 发放账户名称
	 */
	@TableField("grant_account_name")
	private String grantAccountName;
	/**
	 * 付款账号
	 */
	@TableField("payment_account_code")
	private String paymentAccountCode;
	/**
	 * 付款账户名
	 */
	@TableField("payment_account_name")
	private String paymentAccountName;
	/**
	 * 付款银行名
	 */
	@TableField("payment_account_bank_name")
	private String paymentAccountBankName;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
	@TableField("grant_mode")
	private Integer grantMode;
    /**
     * 薪资发放规则编号
     */
	@TableField("salary_grant_rule_id")
	private Long salaryGrantRuleId;
	/**
	 * 规则类型:0-固定金额、1-固定比例
	 */
	@TableField("rule_type")
	private Integer ruleType;
	/**
	 * 规则金额
	 */
	@TableField("rule_amount")
	private BigDecimal ruleAmount;
	/**
	 * 规则比例
	 */
	@TableField("rule_ratio")
	private BigDecimal ruleRatio;
    /**
     * 银行卡编号
     */
	@TableField("bankcard_id")
	private Long bankcardId;
    /**
     * 收款人账号
     */
	@TableField("card_num")
	private String cardNum;
    /**
     * 收款人姓名
     */
	@TableField("account_name")
	private String accountName;
    /**
     * 收款行行号
     */
	@TableField("bank_code")
	private String bankCode;
    /**
     * 收款行名称
     */
	@TableField("deposit_bank")
	private String depositBank;
    /**
     * 银行国际代码
     */
	@TableField("swift_code")
	private String swiftCode;
    /**
     * 国际银行账户号码
     */
	private String iban;
    /**
     * 银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
     */
	@TableField("bankcard_type")
	private Integer bankcardType;
	/**
	 * 银行卡省份代码
	 */
	@TableField("bankcard_province_code")
	private String bankcardProvinceCode;
	/**
	 * 银行卡城市代码
	 */
	@TableField("bankcard_city_code")
	private String bankcardCityCode;
    /**
     * 是否默认卡:1-是，0-否
     */
	@TableField("is_default_card")
	private Boolean isDefaultCard;
    /**
     * 应付工资
     */
	@TableField("wage_payable")
	private BigDecimal wagePayable;
    /**
     * 个人社保
     */
	@TableField("personal_social_security")
	private BigDecimal personalSocialSecurity;
    /**
     * 个人公积金
     */
	@TableField("individual_provident_fund")
	private BigDecimal individualProvidentFund;
    /**
     * 个人所得税
     */
	@TableField("personal_income_tax")
	private BigDecimal personalIncomeTax;
	/**
	 * 年终奖
	 */
	@TableField("year_end_bonus")
	private BigDecimal yearEndBonus;
    /**
     * 人民币金额
     */
	@TableField("payment_amount_rmb")
	private BigDecimal paymentAmountRMB;
    /**
     * 发放金额
     */
	@TableField("payment_amount")
	private BigDecimal paymentAmount;
    /**
     * 发放币种:CNY-人民币，USD-美元，EUR-欧元
     */
	@TableField("currency_code")
	private String currencyCode;
    /**
     * 汇率
     */
	private BigDecimal exchange;
    /**
     * 国籍
     */
	@TableField("country_code")
	private String countryCode;
    /**
     * 服务周期规则编号
     */
    @TableField("cycle_rule_id")
    private Integer cycleRuleId;
    /**
     * 薪资发放日期
     */
	@TableField("grant_date")
	private String grantDate;
    /**
     * 薪资发放时段:1-上午，2-下午
     */
	@TableField("grant_time")
	private Integer grantTime;
	/**
	 * 发放服务标识:0-薪资发放，1-个税，2-薪资发放+个税
	 */
	@TableField("grant_service_type")
	private Integer grantServiceType;
	/**
	 * 合同类型:0-渠道方合同 1-业务合同 2-内部确认单（FC和AF签订）3-合同附件 4-委托合同
	 */
	@TableField("contract_type")
	private Integer contractType;
	/**
	 * 业务合同编号
	 */
	@TableField("contract_id")
	private String contractId;
	/**
	 * 合同我方
	 */
	@TableField("contract_first_party")
	private String contractFirstParty;
    /**
     * 付社保/公积金标识:0-否，1-是
     */
    @TableField("is_welfare_included")
    private Boolean isWelfareIncluded;
	/**
	 * 薪酬服务费
	 */
	@TableField("service_fee_amount")
	private BigDecimal serviceFeeAmount;
	/**
	 * 申报账户
	 */
	@TableField("declaration_account")
	private String declarationAccount;
	/**
	 * 申报账户类别:1-大库（FC目前服务协议只配置FC大库），2-独立库
	 */
	@TableField("declaration_account_category")
	private Integer declarationAccountCategory;
	/**
	 * 缴纳账户
	 */
	@TableField("contribution_account")
	private String contributionAccount;
	/**
	 * 缴纳账户类别:1-大库（FC目前服务协议只配置FC大库），2-独立库
	 */
	@TableField("contribution_account_category")
	private Integer contributionAccountCategory;
    /**
     * 备注
     */
	private String remark;
    /**
     * 变更日志
     */
	@TableField("change_log")
	private String changeLog;
    /**
     * 调整信息
     */
    @TableField("adjust_compare_info")
    private String adjustCompareInfo;
    /**
     * 发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
     */
	@TableField("grant_status")
	private Integer grantStatus;
    /**
     * 暂缓类型:0-全部暂缓，1-部分暂缓
     */
    @TableField("reprieve_type")
    private Integer reprieveType;
    /**
     * 是否有效:1-有效，0-无效
     */
	@TableField("is_active")
	private Boolean isActive;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 最后修改人
     */
	@TableField("modified_by")
	private String modifiedBy;
    /**
     * 最后修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;


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

	public Long getSalaryGrantRuleId() {
		return salaryGrantRuleId;
	}

	public void setSalaryGrantRuleId(Long salaryGrantRuleId) {
		this.salaryGrantRuleId = salaryGrantRuleId;
	}

	public Long getBankcardId() {
		return bankcardId;
	}

	public void setBankcardId(Long bankcardId) {
		this.bankcardId = bankcardId;
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

	public void setDefaultCard(Boolean isDefaultCard) {
		this.isDefaultCard = isDefaultCard;
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

	public BigDecimal getPaymentAmountRMB() {
		return paymentAmountRMB;
	}

	public void setPaymentAmountRMB(BigDecimal paymentAmountRMB) {
		this.paymentAmountRMB = paymentAmountRMB;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getChangeLog() {
		return changeLog;
	}

	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}

	public Integer getGrantStatus() {
		return grantStatus;
	}

	public void setGrantStatus(Integer grantStatus) {
		this.grantStatus = grantStatus;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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

	@Override
	protected Serializable pkVal() {
		return this.salaryGrantEmployeeId;
	}

	public String getGrantAccountName() {
		return grantAccountName;
	}

	public void setGrantAccountName(String grantAccountName) {
		this.grantAccountName = grantAccountName;
	}

	public BigDecimal getYearEndBonus() {
		return yearEndBonus;
	}

	public void setYearEndBonus(BigDecimal yearEndBonus) {
		this.yearEndBonus = yearEndBonus;
	}

	public String getPaymentAccountCode() {
		return paymentAccountCode;
	}

	public void setPaymentAccountCode(String paymentAccountCode) {
		this.paymentAccountCode = paymentAccountCode;
	}

	public String getPaymentAccountName() {
		return paymentAccountName;
	}

	public void setPaymentAccountName(String paymentAccountName) {
		this.paymentAccountName = paymentAccountName;
	}

	public String getPaymentAccountBankName() {
		return paymentAccountBankName;
	}

	public void setPaymentAccountBankName(String paymentAccountBankName) {
		this.paymentAccountBankName = paymentAccountBankName;
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

	// 重写克隆方法子列才可以调用
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

	@Override
	public String toString() {
		return "SalaryGrantEmployeePO{" +
			", salaryGrantEmployeeId=" + salaryGrantEmployeeId +
			", employeeId=" + employeeId +
			", salaryGrantMainTaskCode=" + salaryGrantMainTaskCode +
			", salaryGrantSubTaskCode=" + salaryGrantSubTaskCode +
			", employeeName=" + employeeName +
			", templateType=" + templateType +
			", companyId=" + companyId +
			", companyName=" + companyName +
            ", employeeServiceAgreementId=" + employeeServiceAgreementId +
			", grantCycle=" + grantCycle +
			", taxCycle=" + taxCycle +
			", batchCode=" + batchCode +
			", grantAccountCode=" + grantAccountCode +
			", grantAccountName=" + grantAccountName +
			", paymentAccountCode=" + paymentAccountCode +
			", paymentAccountName=" + paymentAccountName +
			", paymentAccountBankName=" + paymentAccountBankName +
			", grantMode=" + grantMode +
			", salaryGrantRuleId=" + salaryGrantRuleId +
			", ruleType=" + ruleType +
			", ruleAmount=" + ruleAmount +
			", ruleRatio=" + ruleRatio +
			", bankcardId=" + bankcardId +
			", cardNum=" + cardNum +
			", accountName=" + accountName +
			", bankCode=" + bankCode +
			", depositBank=" + depositBank +
			", swiftCode=" + swiftCode +
			", iban=" + iban +
			", bankcardType=" + bankcardType +
			", bankcardProvinceCode=" + bankcardProvinceCode +
			", bankcardCityCode=" + bankcardCityCode +
			", isDefaultCard=" + isDefaultCard +
			", wagePayable=" + wagePayable +
			", personalSocialSecurity=" + personalSocialSecurity +
			", individualProvidentFund=" + individualProvidentFund +
			", personalIncomeTax=" + personalIncomeTax +
			", yearEndBonus=" + yearEndBonus +
			", paymentAmountRMB=" + paymentAmountRMB +
			", paymentAmount=" + paymentAmount +
			", currencyCode=" + currencyCode +
			", exchange=" + exchange +
			", countryCode=" + countryCode +
            ", cycleRuleId=" + cycleRuleId +
			", grantDate=" + grantDate +
			", grantTime=" + grantTime +
            ", grantServiceType=" + grantServiceType +
            ", contractType=" + contractType +
            ", contractId=" + contractId +
            ", contractFirstParty=" + contractFirstParty +
            ", isWelfareIncluded=" + isWelfareIncluded +
			", serviceFeeAmount=" + serviceFeeAmount +
			", declarationAccount=" + declarationAccount +
			", declarationAccountCategory=" + declarationAccountCategory +
			", contributionAccount=" + contributionAccount +
			", contributionAccountCategory=" + contributionAccountCategory +
			", remark=" + remark +
			", changeLog=" + changeLog +
            ", adjustCompareInfo=" + adjustCompareInfo +
			", grantStatus=" + grantStatus +
            ", reprieveType=" + reprieveType +
			", isActive=" + isActive +
			", createdBy=" + createdBy +
			", createdTime=" + createdTime +
			", modifiedBy=" + modifiedBy +
			", modifiedTime=" + modifiedTime +
			"}";
	}
}
