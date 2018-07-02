package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 计算批次明细
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-19
 */
@TableName("tax_fc_calculation_batch_detail")
public class CalculationBatchDetailPO extends Model<CalculationBatchDetailPO> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 计算批次ID（为空，则为合并数据）
	 */
	@TableField("calculation_batch_id")
	private Long calculationBatchId;
	/**
	 * 版本号（重新计算版本号加1）
	 */
	@TableField("version_no")
	private Integer versionNo;
	/**
	 * 是否暂缓
	 */
	@TableField("is_defer")
	private Boolean isDefer;
	/**
	 * 雇员编号
	 */
	@TableField("employee_no")
	private String employeeNo;
	/**
	 * 雇员姓名
	 */
	@TableField("employee_name")
	private String employeeName;
	/**
	 * 证件类型
	 */
	@TableField("id_type")
	private String idType;
	/**
	 * 证件类型中文
	 */
	@TableField(exist = false)
	private String idTypeName;
	/**
	 * 证件编号
	 */
	@TableField("id_no")
	private String idNo;
	/**
	 * 申报账户
	 */
	@TableField("declare_account")
	private String declareAccount;
	/**
	 * 缴纳账户
	 */
	@TableField("pay_account")
	private String payAccount;
	/**
	 * 所得期间
	 */
	private LocalDate period;
	/**
	 * 所得项目
	 */
	@TableField("income_subject")
	private String incomeSubject;
	/**
	 * 所得项目中文
	 */
	@TableField(exist = false)
	private String incomeSubjectName;
	/**
	 * 收入额
	 */
	@TableField("income_total")
	private BigDecimal incomeTotal;
	/**
	 * 免税所得
	 */
	@TableField("income_dutyfree")
	private BigDecimal incomeDutyfree;
	/**
	 * 基本养老保险费（税前扣除项目）
	 */
	@TableField("deduct_retirement_insurance")
	private BigDecimal deductRetirementInsurance;
	/**
	 * 基本医疗保险费（税前扣除项目）
	 */
	@TableField("deduct_medical_insurance")
	private BigDecimal deductMedicalInsurance;
	/**
	 * 失业保险费（税前扣除项目）
	 */
	@TableField("deduct_dleness_insurance")
	private BigDecimal deductDlenessInsurance;
	/**
	 * 财产原值（税前扣除项目）
	 */
	@TableField("deduct_property")
	private BigDecimal deductProperty;
	/**
	 * 住房公积金（税前扣除项目）
	 */
	@TableField("deduct_house_fund")
	private BigDecimal deductHouseFund;
	/**
	 * 允许扣除的税费（税前扣除项目）
	 */
	@TableField("deduct_takeoff")
	private BigDecimal deductTakeoff;
	/**
	 * 其他（税前扣除项目）
	 */
	@TableField("deduct_other")
	private BigDecimal deductOther;
	/**
	 * 合计（税前扣除项目）
	 */
	@TableField("deduct_total")
	private BigDecimal deductTotal;
	/**
	 * 减除费用
	 */
	private BigDecimal deduction;
	/**
	 * 准予扣除的捐赠额
	 */
	private BigDecimal donation;
	/**
	 * 应纳税所得额
	 */
	@TableField("income_for_tax")
	private BigDecimal incomeForTax;
	/**
	 * 税率
	 */
	@TableField("tax_rate")
	private BigDecimal taxRate;
	/**
	 * 速算扣除数
	 */
	@TableField("quick_cal_deduct")
	private BigDecimal quickCalDeduct;
	/**
	 * 应纳税额
	 */
	@TableField("tax_amount")
	private BigDecimal taxAmount;
	/**
	 * 减免税额
	 */
	@TableField("tax_deduction")
	private BigDecimal taxDeduction;
	/**
	 * 应扣缴税额
	 */
	@TableField("tax_withhold_amount")
	private BigDecimal taxWithholdAmount;
	/**
	 * 已扣缴税额
	 */
	@TableField("tax_withholded_amount")
	private BigDecimal taxWithholdedAmount;
	/**
	 * 应补（退）税额
	 */
	@TableField("tax_remedy_or_return")
	private BigDecimal taxRemedyOrReturn;
	/**
	 * 创建时间
	 */
	@TableField(value="created_time",fill = FieldFill.INSERT)
	private LocalDateTime createdTime;
	/**
	 * 修改时间
	 */
	@TableField(value="modified_time",fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime modifiedTime;

	/**
	 * 税金
	 */
	private BigDecimal taxReal;
	/**
	 * 年金
	 */
	private BigDecimal annuity;
	/**
	 * 实际工作年限数
	 */
	private String workingYears;
	/**
	 * 商业健康保险费
	 */
	private BigDecimal businessHealthInsurance;
	/**
	 * 税延养老保险费
	 */
	private BigDecimal endowmentInsurance;
	/**
	 * 境内天数
	 */
	private String domesticDays;
	/**
	 * 境外天数
	 */
	private String overseasDays;
	/**
	 * 境内所得境内支付
	 */
	private BigDecimal domesticIncomeDomesticPayment;
	/**
	 * 境内所得境外支付
	 */
	private BigDecimal domesticIncomeOverseasPayment;
	/**
	 * 境外所得境内支付
	 */
	private BigDecimal overseasIncomeDomesticPayment;
	/**
	 * 境外所得境外支付
	 */
	private BigDecimal overseasIncomeOverseasPayment;
	/**
	 * 住房补贴
	 */
	private BigDecimal housingSubsidy;
	/**
	 * 伙食补贴
	 */
	private BigDecimal mealAllowance;
	/**
	 * 洗衣费
	 */
	private BigDecimal laundryFee;
	/**
	 * 搬迁费
	 */
	private BigDecimal removingIndemnityFee;
	/**
	 * 出差补贴
	 */
	private BigDecimal missionallowance;
	/**
	 * 探亲费
	 */
	private BigDecimal visitingRelativesFee;
	/**
	 * 语言培训费
	 */
	private BigDecimal languageTrainingFee;
	/**
	 * 子女教育经费
	 */
	private BigDecimal educationFunds;
	/**
	 * 本月行权收入
	 */
	private BigDecimal exerciseIncomeMonth;
	/**
	 * 本年度累计行权收入(不含本月)
	 */
	private BigDecimal exerciseIncomeYear;
	/**
	 * 规定月份数
	 */
	private String numberOfMonths;
	/**
	 * 本年累计已纳税额
	 */
	private BigDecimal exerciseTaxAmount;
	/**
	 * 是否可用
	 */
	@TableLogic
	private Boolean isActive;

	/**
	 * 创建人
	 */
	@TableField(value="created_by",fill = FieldFill.INSERT)
	private String createdBy;
	/**
	 * 修改人
	 */
	@TableField(value="modified_by",fill = FieldFill.INSERT_UPDATE)
	private String modifiedBy;
	/**
	 * 创建人displayname
	 */
	@TableField(value="created_by_display_name",fill = FieldFill.INSERT)
	private String createdByDisplayName;
	/**
	 * 修改人displayname
	 */
	@TableField(value="modified_by_display_name",fill = FieldFill.INSERT_UPDATE)
	private String modifiedByDisplayName;

	/**
	 * 批次号
	 */
	@TableField("batch_no")
	private String batchNo;

	//税前合计
	private BigDecimal preTaxAggregate;

	//免税津贴
	private BigDecimal dutyFreeAllowance;

	//其他扣除
	private BigDecimal others;

	public BigDecimal getOthers() {
		return others;
	}

	public void setOthers(BigDecimal others) {
		this.others = others;
	}

	public String getNumberOfMonths() {
		return numberOfMonths;
	}

	public String getCreatedByDisplayName() {
		return createdByDisplayName;
	}

	public void setCreatedByDisplayName(String createdByDisplayName) {
		this.createdByDisplayName = createdByDisplayName;
	}

	public String getModifiedByDisplayName() {
		return modifiedByDisplayName;
	}

	public void setModifiedByDisplayName(String modifiedByDisplayName) {
		this.modifiedByDisplayName = modifiedByDisplayName;
	}

	public BigDecimal getPreTaxAggregate() {
		return preTaxAggregate;
	}

	public void setPreTaxAggregate(BigDecimal preTaxAggregate) {
		this.preTaxAggregate = preTaxAggregate;
	}

	public BigDecimal getDutyFreeAllowance() {
		return dutyFreeAllowance;
	}

	public void setDutyFreeAllowance(BigDecimal dutyFreeAllowance) {
		this.dutyFreeAllowance = dutyFreeAllowance;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public BigDecimal getTaxReal() {
		return taxReal;
	}

	public void setTaxReal(BigDecimal taxReal) {
		this.taxReal = taxReal;
	}

	public BigDecimal getAnnuity() {
		return annuity;
	}

	public void setAnnuity(BigDecimal annuity) {
		this.annuity = annuity;
	}

	public String getWorkingYears() {
		return workingYears;
	}

	public void setWorkingYears(String workingYears) {
		this.workingYears = workingYears;
	}

	public BigDecimal getBusinessHealthInsurance() {
		return businessHealthInsurance;
	}

	public void setBusinessHealthInsurance(BigDecimal businessHealthInsurance) {
		this.businessHealthInsurance = businessHealthInsurance;
	}

	public BigDecimal getEndowmentInsurance() {
		return endowmentInsurance;
	}

	public void setEndowmentInsurance(BigDecimal endowmentInsurance) {
		this.endowmentInsurance = endowmentInsurance;
	}

	public String getDomesticDays() {
		return domesticDays;
	}

	public void setDomesticDays(String domesticDays) {
		this.domesticDays = domesticDays;
	}

	public String getOverseasDays() {
		return overseasDays;
	}

	public void setOverseasDays(String overseasDays) {
		this.overseasDays = overseasDays;
	}

	public void setNumberOfMonths(String numberOfMonths) {
		this.numberOfMonths = numberOfMonths;
	}

	public BigDecimal getDomesticIncomeDomesticPayment() {
		return domesticIncomeDomesticPayment;
	}

	public void setDomesticIncomeDomesticPayment(BigDecimal domesticIncomeDomesticPayment) {
		this.domesticIncomeDomesticPayment = domesticIncomeDomesticPayment;
	}

	public BigDecimal getDomesticIncomeOverseasPayment() {
		return domesticIncomeOverseasPayment;
	}

	public void setDomesticIncomeOverseasPayment(BigDecimal domesticIncomeOverseasPayment) {
		this.domesticIncomeOverseasPayment = domesticIncomeOverseasPayment;
	}

	public BigDecimal getOverseasIncomeDomesticPayment() {
		return overseasIncomeDomesticPayment;
	}

	public void setOverseasIncomeDomesticPayment(BigDecimal overseasIncomeDomesticPayment) {
		this.overseasIncomeDomesticPayment = overseasIncomeDomesticPayment;
	}

	public BigDecimal getOverseasIncomeOverseasPayment() {
		return overseasIncomeOverseasPayment;
	}

	public void setOverseasIncomeOverseasPayment(BigDecimal overseasIncomeOverseasPayment) {
		this.overseasIncomeOverseasPayment = overseasIncomeOverseasPayment;
	}

	public BigDecimal getHousingSubsidy() {
		return housingSubsidy;
	}

	public void setHousingSubsidy(BigDecimal housingSubsidy) {
		this.housingSubsidy = housingSubsidy;
	}

	public BigDecimal getMealAllowance() {
		return mealAllowance;
	}

	public void setMealAllowance(BigDecimal mealAllowance) {
		this.mealAllowance = mealAllowance;
	}

	public BigDecimal getLaundryFee() {
		return laundryFee;
	}

	public void setLaundryFee(BigDecimal laundryFee) {
		this.laundryFee = laundryFee;
	}

	public BigDecimal getRemovingIndemnityFee() {
		return removingIndemnityFee;
	}

	public void setRemovingIndemnityFee(BigDecimal removingIndemnityFee) {
		this.removingIndemnityFee = removingIndemnityFee;
	}

	public BigDecimal getMissionallowance() {
		return missionallowance;
	}

	public void setMissionallowance(BigDecimal missionallowance) {
		this.missionallowance = missionallowance;
	}

	public BigDecimal getVisitingRelativesFee() {
		return visitingRelativesFee;
	}

	public void setVisitingRelativesFee(BigDecimal visitingRelativesFee) {
		this.visitingRelativesFee = visitingRelativesFee;
	}

	public BigDecimal getLanguageTrainingFee() {
		return languageTrainingFee;
	}

	public void setLanguageTrainingFee(BigDecimal languageTrainingFee) {
		this.languageTrainingFee = languageTrainingFee;
	}

	public BigDecimal getEducationFunds() {
		return educationFunds;
	}

	public void setEducationFunds(BigDecimal educationFunds) {
		this.educationFunds = educationFunds;
	}

	public BigDecimal getExerciseIncomeMonth() {
		return exerciseIncomeMonth;
	}

	public void setExerciseIncomeMonth(BigDecimal exerciseIncomeMonth) {
		this.exerciseIncomeMonth = exerciseIncomeMonth;
	}

	public BigDecimal getExerciseIncomeYear() {
		return exerciseIncomeYear;
	}

	public void setExerciseIncomeYear(BigDecimal exerciseIncomeYear) {
		this.exerciseIncomeYear = exerciseIncomeYear;
	}

	public BigDecimal getExerciseTaxAmount() {
		return exerciseTaxAmount;
	}

	public void setExerciseTaxAmount(BigDecimal exerciseTaxAmount) {
		this.exerciseTaxAmount = exerciseTaxAmount;
	}

	public String getIdTypeName() {
		return idTypeName;
	}

	public void setIdTypeName(String idTypeName) {
		this.idTypeName = idTypeName;
	}

	public String getIncomeSubjectName() {
		return incomeSubjectName;
	}

	public void setIncomeSubjectName(String incomeSubjectName) {
		this.incomeSubjectName = incomeSubjectName;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCalculationBatchId() {
		return calculationBatchId;
	}

	public void setCalculationBatchId(Long calculationBatchId) {
		this.calculationBatchId = calculationBatchId;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Boolean getDefer() {
		return isDefer;
	}

	public void setDefer(Boolean defer) {
		isDefer = defer;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getDeclareAccount() {
		return declareAccount;
	}

	public void setDeclareAccount(String declareAccount) {
		this.declareAccount = declareAccount;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public LocalDate getPeriod() {
		return period;
	}

	public void setPeriod(LocalDate period) {
		this.period = period;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getIncomeSubject() {
		return incomeSubject;
	}

	public void setIncomeSubject(String incomeSubject) {
		this.incomeSubject = incomeSubject;
	}

	public BigDecimal getIncomeTotal() {
		return incomeTotal;
	}

	public void setIncomeTotal(BigDecimal incomeTotal) {
		this.incomeTotal = incomeTotal;
	}

	public BigDecimal getIncomeDutyfree() {
		return incomeDutyfree;
	}

	public void setIncomeDutyfree(BigDecimal incomeDutyfree) {
		this.incomeDutyfree = incomeDutyfree;
	}

	public BigDecimal getDeductRetirementInsurance() {
		return deductRetirementInsurance;
	}

	public void setDeductRetirementInsurance(BigDecimal deductRetirementInsurance) {
		this.deductRetirementInsurance = deductRetirementInsurance;
	}

	public BigDecimal getDeductMedicalInsurance() {
		return deductMedicalInsurance;
	}

	public void setDeductMedicalInsurance(BigDecimal deductMedicalInsurance) {
		this.deductMedicalInsurance = deductMedicalInsurance;
	}

	public BigDecimal getDeductDlenessInsurance() {
		return deductDlenessInsurance;
	}

	public void setDeductDlenessInsurance(BigDecimal deductDlenessInsurance) {
		this.deductDlenessInsurance = deductDlenessInsurance;
	}

	public BigDecimal getDeductProperty() {
		return deductProperty;
	}

	public void setDeductProperty(BigDecimal deductProperty) {
		this.deductProperty = deductProperty;
	}

	public BigDecimal getDeductHouseFund() {
		return deductHouseFund;
	}

	public void setDeductHouseFund(BigDecimal deductHouseFund) {
		this.deductHouseFund = deductHouseFund;
	}

	public BigDecimal getDeductTakeoff() {
		return deductTakeoff;
	}

	public void setDeductTakeoff(BigDecimal deductTakeoff) {
		this.deductTakeoff = deductTakeoff;
	}

	public BigDecimal getDeductOther() {
		return deductOther;
	}

	public void setDeductOther(BigDecimal deductOther) {
		this.deductOther = deductOther;
	}

	public BigDecimal getDeductTotal() {
		return deductTotal;
	}

	public void setDeductTotal(BigDecimal deductTotal) {
		this.deductTotal = deductTotal;
	}

	public BigDecimal getDeduction() {
		return deduction;
	}

	public void setDeduction(BigDecimal deduction) {
		this.deduction = deduction;
	}

	public BigDecimal getDonation() {
		return donation;
	}

	public void setDonation(BigDecimal donation) {
		this.donation = donation;
	}

	public BigDecimal getIncomeForTax() {
		return incomeForTax;
	}

	public void setIncomeForTax(BigDecimal incomeForTax) {
		this.incomeForTax = incomeForTax;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getQuickCalDeduct() {
		return quickCalDeduct;
	}

	public void setQuickCalDeduct(BigDecimal quickCalDeduct) {
		this.quickCalDeduct = quickCalDeduct;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getTaxDeduction() {
		return taxDeduction;
	}

	public void setTaxDeduction(BigDecimal taxDeduction) {
		this.taxDeduction = taxDeduction;
	}

	public BigDecimal getTaxWithholdAmount() {
		return taxWithholdAmount;
	}

	public void setTaxWithholdAmount(BigDecimal taxWithholdAmount) {
		this.taxWithholdAmount = taxWithholdAmount;
	}

	public BigDecimal getTaxWithholdedAmount() {
		return taxWithholdedAmount;
	}

	public void setTaxWithholdedAmount(BigDecimal taxWithholdedAmount) {
		this.taxWithholdedAmount = taxWithholdedAmount;
	}

	public BigDecimal getTaxRemedyOrReturn() {
		return taxRemedyOrReturn;
	}

	public void setTaxRemedyOrReturn(BigDecimal taxRemedyOrReturn) {
		this.taxRemedyOrReturn = taxRemedyOrReturn;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CalculationBatchDetailPO{" +
				"id=" + id +
				", calculationBatchId=" + calculationBatchId +
				", versionNo=" + versionNo +
				", isDefer=" + isDefer +
				", employeeNo='" + employeeNo + '\'' +
				", employeeName='" + employeeName + '\'' +
				", idType='" + idType + '\'' +
				", idTypeName='" + idTypeName + '\'' +
				", idNo='" + idNo + '\'' +
				", declareAccount='" + declareAccount + '\'' +
				", payAccount='" + payAccount + '\'' +
				", period=" + period +
				", incomeSubject='" + incomeSubject + '\'' +
				", incomeSubjectName='" + incomeSubjectName + '\'' +
				", incomeTotal=" + incomeTotal +
				", incomeDutyfree=" + incomeDutyfree +
				", deductRetirementInsurance=" + deductRetirementInsurance +
				", deductMedicalInsurance=" + deductMedicalInsurance +
				", deductDlenessInsurance=" + deductDlenessInsurance +
				", deductProperty=" + deductProperty +
				", deductHouseFund=" + deductHouseFund +
				", deductTakeoff=" + deductTakeoff +
				", deductOther=" + deductOther +
				", deductTotal=" + deductTotal +
				", deduction=" + deduction +
				", donation=" + donation +
				", incomeForTax=" + incomeForTax +
				", taxRate='" + taxRate + '\'' +
				", quickCalDeduct=" + quickCalDeduct +
				", taxAmount=" + taxAmount +
				", taxDeduction=" + taxDeduction +
				", taxWithholdAmount=" + taxWithholdAmount +
				", taxWithholdedAmount=" + taxWithholdedAmount +
				", taxRemedyOrReturn=" + taxRemedyOrReturn +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", taxReal=" + taxReal +
				", annuity=" + annuity +
				", workingYears=" + workingYears +
				", businessHealthInsurance=" + businessHealthInsurance +
				", endowmentInsurance=" + endowmentInsurance +
				", domesticDays=" + domesticDays +
				", overseasDays=" + overseasDays +
				", domesticIncomeDomesticPayment=" + domesticIncomeDomesticPayment +
				", domesticIncomeOverseasPayment=" + domesticIncomeOverseasPayment +
				", overseasIncomeDomesticPayment=" + overseasIncomeDomesticPayment +
				", overseasIncomeOverseasPayment=" + overseasIncomeOverseasPayment +
				", housingSubsidy=" + housingSubsidy +
				", mealAllowance=" + mealAllowance +
				", laundryFee=" + laundryFee +
				", removingIndemnityFee=" + removingIndemnityFee +
				", missionallowance=" + missionallowance +
				", visitingRelativesFee=" + visitingRelativesFee +
				", languageTrainingFee=" + languageTrainingFee +
				", educationFunds=" + educationFunds +
				", exerciseIncomeMonth=" + exerciseIncomeMonth +
				", exerciseIncomeYear=" + exerciseIncomeYear +
				", numberOfMonths=" + numberOfMonths +
				", exerciseTaxAmount=" + exerciseTaxAmount +
				", isActive=" + isActive +
				'}';
	}
}
