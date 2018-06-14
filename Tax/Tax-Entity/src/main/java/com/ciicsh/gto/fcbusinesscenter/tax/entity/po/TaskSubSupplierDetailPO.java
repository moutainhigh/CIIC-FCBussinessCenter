package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 供应商子任务明细
 * </p>
 *
 * @author wuhua
 * @since 2018-01-24
 */
@TableName("tax_fc_task_sub_supplier_detail")
public class TaskSubSupplierDetailPO extends Model<TaskSubSupplierDetailPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 供应商子任务ID
     */
	private Long taskSubSupplierId;
    /**
     * 计算批次明细ID
     */
	private Long calculationBatchDetailId;
    /**
     * 版本号
     */
	private Integer versionNo;
    /**
     * 合并后供应商明细ID（非空，则已合并，记录为合并后的明细ID）
     */
	private Long taskSubSupplierDetailId;
    /**
     * 雇员编号
     */
	private String employeeNo;
    /**
     * 雇员姓名
     */
	private String employeeName;
    /**
     * 证件类型
     */
	private String idType;
	/**
	 * 证件类型（中文）
	 */
	@TableField(exist = false)
	private String idTypeName;
    /**
     * 证件编号
     */
	private String idNo;
    /**
     * 申报账户
     */
	private String declareAccount;
    /**
     * 缴纳账户
     */
	private String payAccount;
    /**
     * 个税期间
     */
	private LocalDate period;
    /**
     * 所得项目
     */
	private String incomeSubject;
	/**
	 * 所得项目(中文)
	 */
	@TableField(exist = false)
	private String incomeSubjectName;
    /**
     * 收入额
     */
	private BigDecimal incomeTotal;
    /**
     * 免税所得
     */
	private BigDecimal incomeDutyfree;
    /**
     * 基本养老保险费（税前扣除项目）
     */
	private BigDecimal deductRetirementInsurance;
    /**
     * 基本医疗保险费（税前扣除项目）
     */
	private BigDecimal deductMedicalInsurance;
    /**
     * 失业保险费（税前扣除项目）
     */
	private BigDecimal deductDlenessInsurance;
    /**
     * 财产原值（税前扣除项目）
     */
	private BigDecimal deductProperty;
    /**
     * 住房公积金（税前扣除项目）
     */
	private BigDecimal deductHouseFund;
    /**
     * 允许扣除的税费（税前扣除项目）
     */
	private BigDecimal deductTakeoff;
    /**
     * 其他（税前扣除项目）
     */
	private BigDecimal deductOther;
    /**
     * 合计（税前扣除项目）
     */
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
	private BigDecimal incomeForTax;
    /**
     * 税率
     */
	private String taxRate;
    /**
     * 速算扣除数
     */
	private Integer quickCalDeduct;
    /**
     * 个税总金额
     */
	private BigDecimal taxAmount;
    /**
     * 减免税额
     */
	private BigDecimal taxDeduction;
    /**
     * 应扣缴税额
     */
	private BigDecimal taxWithholdAmount;
    /**
     * 已扣缴税额
     */
	private BigDecimal taxWithholdedAmount;
    /**
     * 应补（退）税额
     */
	private BigDecimal taxRemedyOrReturn;
    /**
     * 是否可用
     */
    @TableLogic
	private Boolean isActive;
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
	 * 主任务明细ID
	 */
	private Long taskMainDetailId;

	/**
	 * 是否为合并明细
	 */
	@TableField("is_combined")
	private Boolean isCombined;
	/**
	 * 合并明细是否已确认
	 */
	@TableField("is_combine_confirmed")
	private Boolean isCombineConfirmed;

	private String managerNo;

	private String managerName;

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
	private Integer workingYears;
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
	private Integer domesticDays;
	/**
	 * 境外天数
	 */
	private Integer overseasDays;
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
	private Integer numberOfMonths;
	/**
	 * 本年累计已纳税额
	 */
	private BigDecimal exerciseTaxAmount;

	//税前合计
	private BigDecimal preTaxAggregate;

	//免税津贴
	private BigDecimal dutyFreeAllowance;

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

	public String getManagerNo() {
		return managerNo;
	}

	public void setManagerNo(String managerNo) {
		this.managerNo = managerNo;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Long getTaskMainDetailId() {
		return taskMainDetailId;
	}

	public void setTaskMainDetailId(Long taskMainDetailId) {
		this.taskMainDetailId = taskMainDetailId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskSubSupplierId() {
		return taskSubSupplierId;
	}

	public void setTaskSubSupplierId(Long taskSubSupplierId) {
		this.taskSubSupplierId = taskSubSupplierId;
	}

	public Long getCalculationBatchDetailId() {
		return calculationBatchDetailId;
	}

	public void setCalculationBatchDetailId(Long calculationBatchDetailId) {
		this.calculationBatchDetailId = calculationBatchDetailId;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Long getTaskSubSupplierDetailId() {
		return taskSubSupplierDetailId;
	}

	public void setTaskSubSupplierDetailId(Long taskSubSupplierDetailId) {
		this.taskSubSupplierDetailId = taskSubSupplierDetailId;
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

		if(idType!=null){

			this.idTypeName  = EnumUtil.getMessage(EnumUtil.IT_TYPE,idType);
		}
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

	public String getIncomeSubject() {
		return incomeSubject;
	}

	public void setIncomeSubject(String incomeSubject) {
		this.incomeSubject = incomeSubject;

		if(incomeSubject!=null){

			this.incomeSubjectName  = EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,incomeSubject);
		}
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

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getQuickCalDeduct() {
		return quickCalDeduct;
	}

	public void setQuickCalDeduct(Integer quickCalDeduct) {
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

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
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

	public Boolean getCombined() {
		return isCombined;
	}

	public void setCombined(Boolean combined) {
		isCombined = combined;
	}

	public Boolean getCombineConfirmed() {
		return isCombineConfirmed;
	}

	public void setCombineConfirmed(Boolean combineConfirmed) {
		isCombineConfirmed = combineConfirmed;
	}


	public String groupBys(){

		return this.employeeNo + DateTimeFormatter.ofPattern("yyyy-MM").format(this.period) + this.incomeSubject;
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

	public Integer getWorkingYears() {
		return workingYears;
	}

	public void setWorkingYears(Integer workingYears) {
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

	public Integer getDomesticDays() {
		return domesticDays;
	}

	public void setDomesticDays(Integer domesticDays) {
		this.domesticDays = domesticDays;
	}

	public Integer getOverseasDays() {
		return overseasDays;
	}

	public void setOverseasDays(Integer overseasDays) {
		this.overseasDays = overseasDays;
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

	public Integer getNumberOfMonths() {
		return numberOfMonths;
	}

	public void setNumberOfMonths(Integer numberOfMonths) {
		this.numberOfMonths = numberOfMonths;
	}

	public BigDecimal getExerciseTaxAmount() {
		return exerciseTaxAmount;
	}

	public void setExerciseTaxAmount(BigDecimal exerciseTaxAmount) {
		this.exerciseTaxAmount = exerciseTaxAmount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TaskSubSupplierDetailPO{" +
				"id=" + id +
				", taskSubSupplierId=" + taskSubSupplierId +
				", calculationBatchDetailId=" + calculationBatchDetailId +
				", versionNo=" + versionNo +
				", taskSubSupplierDetailId=" + taskSubSupplierDetailId +
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
				", isActive=" + isActive +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", createdBy='" + createdBy + '\'' +
				", modifiedBy='" + modifiedBy + '\'' +
				", taskMainDetailId=" + taskMainDetailId +
				", isCombined=" + isCombined +
				", isCombineConfirmed=" + isCombineConfirmed +
				", managerNo='" + managerNo + '\'' +
				", managerName='" + managerName + '\'' +
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
				'}';
	}
}
