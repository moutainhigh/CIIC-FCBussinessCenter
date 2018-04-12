package com.ciicsh.gto.fcbusinesscenter.site.service.api.dto;

import java.io.Serializable;

import java.math.BigDecimal;
import com.ciicsh.gto.fcbusinesscenter.site.service.api.dto.CommonListDTO;

import java.io.Serializable;

/**
 * <p>
 * 薪资计算批次结果表(雇员维度)
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-01
 */
public class PayrollCalcResultDTO extends CommonListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 薪资核算结果流水号ID
     */
	private Long fcPayrollCalcResultId;
    /**
     * 雇员Id, EntityId - YY+6位数字
     */
	private String empId;
	/**
	 * 雇员名称
	 */
	private String empName;
    /**
     * 计算批次编号（EntityId）
     */
	private String batchId;
    /**
     * 参考计算批次编号（EntityId） - 针对调整、回溯计算
     */
	private String refBatchId;
	/**
	 * 批次类型
	 */
	private String batchType;
    /**
     * 管理方id
     */
    private String mgrId;
    /**
     * 管理方名称
     */
    private String mgrName;
    /**
     * 国家编码
     */
    private String countryCode;
    /**
     * 离职年限
     */
	private Integer leavingYears;
    /**
     * 实发工资
     */
	private BigDecimal personnelIncomeNetPay;
    /**
     * 个人所得税
     */
	private BigDecimal personnelIncomeTax;
    /**
     * 个人收入（薪金）- 税前
     */
	private BigDecimal personnelIncomeWageBeforeTax;
    /**
     * 个人收入（薪金）- 税后
     */
	private BigDecimal personnelIncomeWageAfterTax;
    /**
     * 个人收入（年奖）- 税前
     */
	private BigDecimal personnelIncomeYearlyBonusBeforeTax;
    /**
     * 个人收入（年奖）- 税后
     */
	private BigDecimal personnelIncomeYearlyBonusAfterTax;
    /**
     * 个人收入（劳务费）- 税前
     */
	private BigDecimal personnelIncomeLaborBeforeTax;
    /**
     * 个人收入（劳务费）- 税后
     */
	private BigDecimal personnelIncomeLaborAfterTax;
    /**
     * 个人收入（偶然所得）- 税前
     */
	private BigDecimal personnelIncomeAccidentBeforeTax;
    /**
     * 个人收入（偶然所得）- 税后
     */
	private BigDecimal personnelIncomeAccidentAfterTax;
    /**
     * 个人收入（一次性补偿）- 税前
     */
	private BigDecimal personnelIncomeDisposableCompensionBeforeTax;
    /**
     * 个人收入（一次性补偿）- 税后
     */
	private BigDecimal personnelIncomeDisposableCompensionAfterTax;
    /**
     * 个人收入（利息、股息、红利）- 税前
     */
	private BigDecimal personnelIncomeInterestBeforeTax;
    /**
     * 个人收入（利息、股息、红利）- 税后
     */
	private BigDecimal personnelIncomeInterestAfterTax;
    /**
     * 个人收入（股票期权）- 税前
     */
	private BigDecimal personnelIncomeStockOptionBeforeTax;
    /**
     * 个人收入（股票期权）- 税后
     */
	private BigDecimal personnelIncomeStockOptionAfterTax;
    /**
     * 个人收入（财产转让所得）- 税前
     */
	private BigDecimal personnelIncomePropertyTransferBeforeTax;
    /**
     * 个人收入（财产转让所得）- 税后
     */
	private BigDecimal personnelIncomePropertyTransferAfterTax;
    /**
     * 当前批次雇员的个人社保总额（包括医疗保险、养老保险、失业保险）
     */
	private BigDecimal personnelSocialSecurity;
    /**
     * 当前批次中此雇员的个人公积金总额（包括住房公积金、补充公积金）
     */
	private BigDecimal personnelProvidentFund;
    /**
     * 工资年月
     */
	private String personnelIncomeYearMonth;
    /**
     * 个人免税额度
     */
	private BigDecimal personnelTaxExemption;
    /**
     * 报税年月
     */
	private String taxYearMonth;
    /**
     * 年金
     */
	private BigDecimal annuity;
    /**
     * 合同我方：分三种 - AF、FC、BPO，销售中心报价单-》雇员服务协议
     */
	private String contractFirstParty;
    /**
     * 薪资计算结果（雇员维度）
     */
	private String salaryCalcResultItems;
    /**
     * 雇员服务协议
     */
	private String employeeServiceAgreement;
    /**
     * 备注
     */
	private String remark;
    /**
     * 是否可用
     */
	private Boolean isActive;

	public Long getFcPayrollCalcResultId() {
		return fcPayrollCalcResultId;
	}

	public void setFcPayrollCalcResultId(Long fcPayrollCalcResultId) {
		this.fcPayrollCalcResultId = fcPayrollCalcResultId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getRefBatchId() {
		return refBatchId;
	}

	public void setRefBatchId(String refBatchId) {
		this.refBatchId = refBatchId;
	}

	public Integer getLeavingYears() {
		return leavingYears;
	}

	public void setLeavingYears(Integer leavingYears) {
		this.leavingYears = leavingYears;
	}

	public BigDecimal getPersonnelIncomeNetPay() {
		return personnelIncomeNetPay;
	}

	public void setPersonnelIncomeNetPay(BigDecimal personnelIncomeNetPay) {
		this.personnelIncomeNetPay = personnelIncomeNetPay;
	}

	public BigDecimal getPersonnelIncomeTax() {
		return personnelIncomeTax;
	}

	public void setPersonnelIncomeTax(BigDecimal personnelIncomeTax) {
		this.personnelIncomeTax = personnelIncomeTax;
	}

	public BigDecimal getPersonnelIncomeWageBeforeTax() {
		return personnelIncomeWageBeforeTax;
	}

	public void setPersonnelIncomeWageBeforeTax(BigDecimal personnelIncomeWageBeforeTax) {
		this.personnelIncomeWageBeforeTax = personnelIncomeWageBeforeTax;
	}

	public BigDecimal getPersonnelIncomeWageAfterTax() {
		return personnelIncomeWageAfterTax;
	}

	public void setPersonnelIncomeWageAfterTax(BigDecimal personnelIncomeWageAfterTax) {
		this.personnelIncomeWageAfterTax = personnelIncomeWageAfterTax;
	}

	public BigDecimal getPersonnelIncomeYearlyBonusBeforeTax() {
		return personnelIncomeYearlyBonusBeforeTax;
	}

	public void setPersonnelIncomeYearlyBonusBeforeTax(BigDecimal personnelIncomeYearlyBonusBeforeTax) {
		this.personnelIncomeYearlyBonusBeforeTax = personnelIncomeYearlyBonusBeforeTax;
	}

	public BigDecimal getPersonnelIncomeYearlyBonusAfterTax() {
		return personnelIncomeYearlyBonusAfterTax;
	}

	public void setPersonnelIncomeYearlyBonusAfterTax(BigDecimal personnelIncomeYearlyBonusAfterTax) {
		this.personnelIncomeYearlyBonusAfterTax = personnelIncomeYearlyBonusAfterTax;
	}

	public BigDecimal getPersonnelIncomeLaborBeforeTax() {
		return personnelIncomeLaborBeforeTax;
	}

	public void setPersonnelIncomeLaborBeforeTax(BigDecimal personnelIncomeLaborBeforeTax) {
		this.personnelIncomeLaborBeforeTax = personnelIncomeLaborBeforeTax;
	}

	public BigDecimal getPersonnelIncomeLaborAfterTax() {
		return personnelIncomeLaborAfterTax;
	}

	public void setPersonnelIncomeLaborAfterTax(BigDecimal personnelIncomeLaborAfterTax) {
		this.personnelIncomeLaborAfterTax = personnelIncomeLaborAfterTax;
	}

	public BigDecimal getPersonnelIncomeAccidentBeforeTax() {
		return personnelIncomeAccidentBeforeTax;
	}

	public void setPersonnelIncomeAccidentBeforeTax(BigDecimal personnelIncomeAccidentBeforeTax) {
		this.personnelIncomeAccidentBeforeTax = personnelIncomeAccidentBeforeTax;
	}

	public BigDecimal getPersonnelIncomeAccidentAfterTax() {
		return personnelIncomeAccidentAfterTax;
	}

	public void setPersonnelIncomeAccidentAfterTax(BigDecimal personnelIncomeAccidentAfterTax) {
		this.personnelIncomeAccidentAfterTax = personnelIncomeAccidentAfterTax;
	}

	public BigDecimal getPersonnelIncomeDisposableCompensionBeforeTax() {
		return personnelIncomeDisposableCompensionBeforeTax;
	}

	public void setPersonnelIncomeDisposableCompensionBeforeTax(BigDecimal personnelIncomeDisposableCompensionBeforeTax) {
		this.personnelIncomeDisposableCompensionBeforeTax = personnelIncomeDisposableCompensionBeforeTax;
	}

	public BigDecimal getPersonnelIncomeDisposableCompensionAfterTax() {
		return personnelIncomeDisposableCompensionAfterTax;
	}

	public void setPersonnelIncomeDisposableCompensionAfterTax(BigDecimal personnelIncomeDisposableCompensionAfterTax) {
		this.personnelIncomeDisposableCompensionAfterTax = personnelIncomeDisposableCompensionAfterTax;
	}

	public BigDecimal getPersonnelIncomeInterestBeforeTax() {
		return personnelIncomeInterestBeforeTax;
	}

	public void setPersonnelIncomeInterestBeforeTax(BigDecimal personnelIncomeInterestBeforeTax) {
		this.personnelIncomeInterestBeforeTax = personnelIncomeInterestBeforeTax;
	}

	public BigDecimal getPersonnelIncomeInterestAfterTax() {
		return personnelIncomeInterestAfterTax;
	}

	public void setPersonnelIncomeInterestAfterTax(BigDecimal personnelIncomeInterestAfterTax) {
		this.personnelIncomeInterestAfterTax = personnelIncomeInterestAfterTax;
	}

	public BigDecimal getPersonnelIncomeStockOptionBeforeTax() {
		return personnelIncomeStockOptionBeforeTax;
	}

	public void setPersonnelIncomeStockOptionBeforeTax(BigDecimal personnelIncomeStockOptionBeforeTax) {
		this.personnelIncomeStockOptionBeforeTax = personnelIncomeStockOptionBeforeTax;
	}

	public BigDecimal getPersonnelIncomeStockOptionAfterTax() {
		return personnelIncomeStockOptionAfterTax;
	}

	public void setPersonnelIncomeStockOptionAfterTax(BigDecimal personnelIncomeStockOptionAfterTax) {
		this.personnelIncomeStockOptionAfterTax = personnelIncomeStockOptionAfterTax;
	}

	public BigDecimal getPersonnelIncomePropertyTransferBeforeTax() {
		return personnelIncomePropertyTransferBeforeTax;
	}

	public void setPersonnelIncomePropertyTransferBeforeTax(BigDecimal personnelIncomePropertyTransferBeforeTax) {
		this.personnelIncomePropertyTransferBeforeTax = personnelIncomePropertyTransferBeforeTax;
	}

	public BigDecimal getPersonnelIncomePropertyTransferAfterTax() {
		return personnelIncomePropertyTransferAfterTax;
	}

	public void setPersonnelIncomePropertyTransferAfterTax(BigDecimal personnelIncomePropertyTransferAfterTax) {
		this.personnelIncomePropertyTransferAfterTax = personnelIncomePropertyTransferAfterTax;
	}

	public BigDecimal getPersonnelSocialSecurity() {
		return personnelSocialSecurity;
	}

	public void setPersonnelSocialSecurity(BigDecimal personnelSocialSecurity) {
		this.personnelSocialSecurity = personnelSocialSecurity;
	}

	public BigDecimal getPersonnelProvidentFund() {
		return personnelProvidentFund;
	}

	public void setPersonnelProvidentFund(BigDecimal personnelProvidentFund) {
		this.personnelProvidentFund = personnelProvidentFund;
	}

	public String getPersonnelIncomeYearMonth() {
		return personnelIncomeYearMonth;
	}

	public void setPersonnelIncomeYearMonth(String personnelIncomeYearMonth) {
		this.personnelIncomeYearMonth = personnelIncomeYearMonth;
	}

	public BigDecimal getPersonnelTaxExemption() {
		return personnelTaxExemption;
	}

	public void setPersonnelTaxExemption(BigDecimal personnelTaxExemption) {
		this.personnelTaxExemption = personnelTaxExemption;
	}

	public String getTaxYearMonth() {
		return taxYearMonth;
	}

	public void setTaxYearMonth(String taxYearMonth) {
		this.taxYearMonth = taxYearMonth;
	}

	public BigDecimal getAnnuity() {
		return annuity;
	}

	public void setAnnuity(BigDecimal annuity) {
		this.annuity = annuity;
	}

	public String getContractFirstParty() {
		return contractFirstParty;
	}

	public void setContractFirstParty(String contractFirstParty) {
		this.contractFirstParty = contractFirstParty;
	}

	public String getSalaryCalcResultItems() {
		return salaryCalcResultItems;
	}

	public void setSalaryCalcResultItems(String salaryCalcResultItems) {
		this.salaryCalcResultItems = salaryCalcResultItems;
	}

	public String getEmployeeServiceAgreement() {
		return employeeServiceAgreement;
	}

	public void setEmployeeServiceAgreement(String employeeServiceAgreement) {
		this.employeeServiceAgreement = employeeServiceAgreement;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getMgrId() {
        return mgrId;
    }

    public void setMgrId(String mgrId) {
        this.mgrId = mgrId;
    }

    public String getMgrName() {
        return mgrName;
    }

    public void setMgrName(String mgrName) {
        this.mgrName = mgrName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
	public String toString() {
		return "FcPayrollCalcResult{" +
			", fcPayrollCalcResultId=" + fcPayrollCalcResultId +
			", empId=" + empId +
			", batchId=" + batchId +
			", refBatchId=" + refBatchId +
			", leavingYears=" + leavingYears +
			", personnelIncomeNetPay=" + personnelIncomeNetPay +
			", personnelIncomeTax=" + personnelIncomeTax +
			", personnelIncomeWageBeforeTax=" + personnelIncomeWageBeforeTax +
			", personnelIncomeWageAfterTax=" + personnelIncomeWageAfterTax +
			", personnelIncomeYearlyBonusBeforeTax=" + personnelIncomeYearlyBonusBeforeTax +
			", personnelIncomeYearlyBonusAfterTax=" + personnelIncomeYearlyBonusAfterTax +
			", personnelIncomeLaborBeforeTax=" + personnelIncomeLaborBeforeTax +
			", personnelIncomeLaborAfterTax=" + personnelIncomeLaborAfterTax +
			", personnelIncomeAccidentBeforeTax=" + personnelIncomeAccidentBeforeTax +
			", personnelIncomeAccidentAfterTax=" + personnelIncomeAccidentAfterTax +
			", personnelIncomeDisposableCompensionBeforeTax=" + personnelIncomeDisposableCompensionBeforeTax +
			", personnelIncomeDisposableCompensionAfterTax=" + personnelIncomeDisposableCompensionAfterTax +
			", personnelIncomeInterestBeforeTax=" + personnelIncomeInterestBeforeTax +
			", personnelIncomeInterestAfterTax=" + personnelIncomeInterestAfterTax +
			", personnelIncomeStockOptionBeforeTax=" + personnelIncomeStockOptionBeforeTax +
			", personnelIncomeStockOptionAfterTax=" + personnelIncomeStockOptionAfterTax +
			", personnelIncomePropertyTransferBeforeTax=" + personnelIncomePropertyTransferBeforeTax +
			", personnelIncomePropertyTransferAfterTax=" + personnelIncomePropertyTransferAfterTax +
			", personnelSocialSecurity=" + personnelSocialSecurity +
			", personnelProvidentFund=" + personnelProvidentFund +
			", personnelIncomeYearMonth=" + personnelIncomeYearMonth +
			", personnelTaxExemption=" + personnelTaxExemption +
			", taxYearMonth=" + taxYearMonth +
			", annuity=" + annuity +
			", contractFirstParty=" + contractFirstParty +
			", salaryCalcResultItems=" + salaryCalcResultItems +
			", employeeServiceAgreement=" + employeeServiceAgreement +
			", remark=" + remark +
			", isActive=" + isActive +
            ", empName=" + empName +
            ", batchType=" + batchType +
            ", mgrId=" + mgrId +
            ", mgrName=" + mgrName +
            ", countryCode=" + countryCode +
			"}";
	}
}
