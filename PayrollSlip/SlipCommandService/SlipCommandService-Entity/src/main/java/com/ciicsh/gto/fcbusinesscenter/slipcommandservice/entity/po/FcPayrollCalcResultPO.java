package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 薪资计算批次结果表(雇员维度)
 *
 * @author taka
 * @since 2018-02-12
 */
@TableName("fc_payroll_calc_result")
public class FcPayrollCalcResultPO extends Model<FcPayrollCalcResultPO> {

  private static final long serialVersionUID = 1L;

  /**
   * 薪资核算结果流水号ID
   */
  @TableId(value="fc_payroll_calc_result_id", type=IdType.AUTO)
  private Long fcPayrollCalcResultId;

  /**
   * 雇员Id, EntityId - YY+6位数字
   */
  @TableField("emp_id")
  private String empId;

  /**
   * 雇员姓名
   */
  @TableField("emp_name")
  private String empName;

  /**
   * 工资单类型:0-通用，1-纸质，2-邮件，3-网上查看
   */
  @TableField("payroll_type")
  private Integer payrollType;

  /**
   * 部门
   */
  @TableField("department")
  private String department;

  /**
   * 国家代码
   */
  @TableField("country_code")
  private String countryCode;

  /**
   * 计算批次编号（EntityId）
   */
  @TableField("batch_id")
  private String batchId;

  /**
   * 参考计算批次编号（EntityId） - 针对调整、回溯计算
   */
  @TableField("ref_batch_id")
  private String refBatchId;

  /**
   * 离职年限
   */
  @TableField("leaving_years")
  private Integer leavingYears;

  /**
   * 实发工资
   */
  @TableField("personnel_income_net_pay")
  private BigDecimal personnelIncomeNetPay;

  /**
   * 个人所得税
   */
  @TableField("personnel_income_tax")
  private BigDecimal personnelIncomeTax;

  /**
   * 个人收入（薪金）- 税前
   */
  @TableField("personnel_income_wage_before_tax")
  private BigDecimal personnelIncomeWageBeforeTax;

  /**
   * 个人收入（薪金）- 税后
   */
  @TableField("personnel_income_wage_after_tax")
  private BigDecimal personnelIncomeWageAfterTax;

  /**
   * 个人收入（年奖）- 税前
   */
  @TableField("personnel_income_yearly_bonus_before_tax")
  private BigDecimal personnelIncomeYearlyBonusBeforeTax;

  /**
   * 个人收入（年奖）- 税后
   */
  @TableField("personnel_income_yearly_bonus_after_tax")
  private BigDecimal personnelIncomeYearlyBonusAfterTax;

  /**
   * 个人收入（劳务费）- 税前
   */
  @TableField("personnel_income_labor_before_tax")
  private BigDecimal personnelIncomeLaborBeforeTax;

  /**
   * 个人收入（劳务费）- 税后
   */
  @TableField("personnel_income_labor_after_tax")
  private BigDecimal personnelIncomeLaborAfterTax;

  /**
   * 个人收入（偶然所得）- 税前
   */
  @TableField("personnel_income_accident_before_tax")
  private BigDecimal personnelIncomeAccidentBeforeTax;

  /**
   * 个人收入（偶然所得）- 税后
   */
  @TableField("personnel_income_accident_after_tax")
  private BigDecimal personnelIncomeAccidentAfterTax;

  /**
   * 个人收入（一次性补偿）- 税前
   */
  @TableField("personnel_income_disposable_compension_before_tax")
  private BigDecimal personnelIncomeDisposableCompensionBeforeTax;

  /**
   * 个人收入（一次性补偿）- 税后
   */
  @TableField("personnel_income_disposable_compension_after_tax")
  private BigDecimal personnelIncomeDisposableCompensionAfterTax;

  /**
   * 个人收入（利息、股息、红利）- 税前
   */
  @TableField("personnel_income_interest_before_tax")
  private BigDecimal personnelIncomeInterestBeforeTax;

  /**
   * 个人收入（利息、股息、红利）- 税后
   */
  @TableField("personnel_income_interest_after_tax")
  private BigDecimal personnelIncomeInterestAfterTax;

  /**
   * 个人收入（股票期权）- 税前
   */
  @TableField("personnel_income_stock_option_before_tax")
  private BigDecimal personnelIncomeStockOptionBeforeTax;

  /**
   * 个人收入（股票期权）- 税后
   */
  @TableField("personnel_income_stock_option_after_tax")
  private BigDecimal personnelIncomeStockOptionAfterTax;

  /**
   * 个人收入（财产转让所得）- 税前
   */
  @TableField("personnel_income_property_transfer_before_tax")
  private BigDecimal personnelIncomePropertyTransferBeforeTax;

  /**
   * 个人收入（财产转让所得）- 税后
   */
  @TableField("personnel_income_property_transfer_after_tax")
  private BigDecimal personnelIncomePropertyTransferAfterTax;

  /**
   * 当前批次雇员的个人社保总额（包括医疗保险、养老保险、失业保险）
   */
  @TableField("personnel_social_security")
  private BigDecimal personnelSocialSecurity;

  /**
   * 当前批次中此雇员的个人公积金总额（包括住房公积金、补充公积金）
   */
  @TableField("personnel_provident_fund")
  private BigDecimal personnelProvidentFund;

  /**
   * 工资年月
   */
  @TableField("personnel_income_year_month")
  private String personnelIncomeYearMonth;

  /**
   * 个人免税额度
   */
  @TableField("personnel_tax_exemption")
  private BigDecimal personnelTaxExemption;

  /**
   * 报税年月
   */
  @TableField("tax_year_month")
  private String taxYearMonth;

  /**
   * 年金
   */
  @TableField("annuity")
  private BigDecimal annuity;

  /**
   * 合同我方：分三种 - AF、FC、BPO，销售中心报价单-》雇员服务协议
   */
  @TableField("contract_first_party")
  private String contractFirstParty;

  /**
   * 薪资计算结果（雇员维度）
   */
  @TableField("salary_calc_result_items")
  private String salaryCalcResultItems;

  /**
   * 雇员服务协议
   */
  @TableField("employee_service_agreement")
  private String employeeServiceAgreement;

  /**
   * 备注
   */
  @TableField("remark")
  private String remark;

  /**
   * 是否可用
   */
  @TableField("is_active")
  private Boolean isActive;

  /**
   * 创建者
   */
  @TableField("created_by")
  private String createdBy;

  /**
   * 创建时间
   */
  @TableField("created_time")
  private Date createdTime;

  /**
   * 最后修改者
   */
  @TableField("modified_by")
  private String modifiedBy;

  /**
   * 最后修改时间
   */
  @TableField("modified_time")
  private Date modifiedTime;



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

  public String getEmpName() {
    return empName;
  }

  public void setEmpName(String empName) {
    this.empName = empName;
  }

  public Integer getPayrollType() {
    return payrollType;
  }

  public void setPayrollType(Integer payrollType) {
    this.payrollType = payrollType;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
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

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
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



  @Override
  protected Serializable pkVal() {
    return this.fcPayrollCalcResultId;
  }

  @Override
  public String toString() {
    return "FcPayrollCalcResultPO{" +
            "fcPayrollCalcResultId=" + fcPayrollCalcResultId +
            ", empId=" + empId +
            ", empName=" + empName +
            ", payrollType=" + payrollType +
            ", department=" + department +
            ", countryCode=" + countryCode +
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
            ", createdBy=" + createdBy +
            ", createdTime=" + createdTime +
            ", modifiedBy=" + modifiedBy +
            ", modifiedTime=" + modifiedTime +
            "}";
  }
}
