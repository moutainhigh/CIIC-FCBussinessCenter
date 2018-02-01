package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
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
     * 证件类型（中文）
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
    @TableField(exist = false)
	private String incomeSubjectName;
    /**
     * 所得项目（中文）
     */
	@TableField("income_subject")
	private String incomeSubject;
    /**
     * 收入额
     */
	@TableField("income_total")
	private String incomeTotal;
    /**
     * 免税所得
     */
	@TableField("income_dutyfree")
	private String incomeDutyfree;
    /**
     * 基本养老保险费（税前扣除项目）
     */
	@TableField("deduct_retirement_insurance")
	private String deductRetirementInsurance;
    /**
     * 基本医疗保险费（税前扣除项目）
     */
	@TableField("deduct_medical_insurance")
	private String deductMedicalInsurance;
    /**
     * 失业保险费（税前扣除项目）
     */
	@TableField("deduct_dleness_insurance")
	private String deductDlenessInsurance;
    /**
     * 财产原值（税前扣除项目）
     */
	@TableField("deduct_property")
	private String deductProperty;
    /**
     * 住房公积金（税前扣除项目）
     */
	@TableField("deduct_house_fund")
	private String deductHouseFund;
    /**
     * 允许扣除的税费（税前扣除项目）
     */
	@TableField("deduct_takeoff")
	private String deductTakeoff;
    /**
     * 其他（税前扣除项目）
     */
	@TableField("deduct_other")
	private String deductOther;
    /**
     * 合计（税前扣除项目）
     */
	@TableField("deduct_total")
	private String deductTotal;
    /**
     * 减除费用
     */
	private String deduction;
    /**
     * 准予扣除的捐赠额
     */
	private String donation;
    /**
     * 应纳税所得额
     */
	@TableField("income_for_tax")
	private String incomeForTax;
    /**
     * 税率
     */
	@TableField("tax_rate")
	private String taxRate;
    /**
     * 速算扣除数
     */
	@TableField("quick_cal_deduct")
	private Integer quickCalDeduct;
    /**
     * 应纳税额
     */
	@TableField("tax_amount")
	private String taxAmount;
    /**
     * 减免税额
     */
	@TableField("tax_deduction")
	private String taxDeduction;
    /**
     * 应扣缴税额
     */
	@TableField("tax_withhold_amount")
	private String taxWithholdAmount;
    /**
     * 已扣缴税额
     */
	@TableField("tax_withholded_amount")
	private String taxWithholdedAmount;
    /**
     * 应补（退）税额
     */
	@TableField("tax_remedy_or_return")
	private String taxRemedyOrReturn;
    /**
     * 是否申报
     */
	@TableField("is_declare")
	private Boolean isDeclare;
    /**
     * 是否划款
     */
	@TableField("is_tranfer")
	private Boolean isTranfer;
    /**
     * 是否缴纳
     */
	@TableField("is_pay")
	private Boolean isPay;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private LocalDateTime createdTime;
    /**
     * 修改时间
     */
	@TableField("modified_time")
	private LocalDateTime modifiedTime;


	public String getIdTypeName() {
		return idTypeName;
	}

	public void setIdTypeName(String idTypeName) {
		this.idTypeName = idTypeName;
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

	public String getIncomeSubject() {
		return incomeSubject;
	}

	public void setIncomeSubject(String incomeSubject) {
		this.incomeSubject = incomeSubject;
	}

	public String getIncomeTotal() {
		return incomeTotal;
	}

	public void setIncomeTotal(String incomeTotal) {
		this.incomeTotal = incomeTotal;
	}

	public String getIncomeDutyfree() {
		return incomeDutyfree;
	}

	public void setIncomeDutyfree(String incomeDutyfree) {
		this.incomeDutyfree = incomeDutyfree;
	}

	public String getDeductRetirementInsurance() {
		return deductRetirementInsurance;
	}

	public void setDeductRetirementInsurance(String deductRetirementInsurance) {
		this.deductRetirementInsurance = deductRetirementInsurance;
	}

	public String getDeductMedicalInsurance() {
		return deductMedicalInsurance;
	}

	public void setDeductMedicalInsurance(String deductMedicalInsurance) {
		this.deductMedicalInsurance = deductMedicalInsurance;
	}

	public String getDeductDlenessInsurance() {
		return deductDlenessInsurance;
	}

	public void setDeductDlenessInsurance(String deductDlenessInsurance) {
		this.deductDlenessInsurance = deductDlenessInsurance;
	}

	public String getDeductProperty() {
		return deductProperty;
	}

	public void setDeductProperty(String deductProperty) {
		this.deductProperty = deductProperty;
	}

	public String getDeductHouseFund() {
		return deductHouseFund;
	}

	public void setDeductHouseFund(String deductHouseFund) {
		this.deductHouseFund = deductHouseFund;
	}

	public String getDeductTakeoff() {
		return deductTakeoff;
	}

	public void setDeductTakeoff(String deductTakeoff) {
		this.deductTakeoff = deductTakeoff;
	}

	public String getDeductOther() {
		return deductOther;
	}

	public void setDeductOther(String deductOther) {
		this.deductOther = deductOther;
	}

	public String getDeductTotal() {
		return deductTotal;
	}

	public void setDeductTotal(String deductTotal) {
		this.deductTotal = deductTotal;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public String getDonation() {
		return donation;
	}

	public void setDonation(String donation) {
		this.donation = donation;
	}

	public String getIncomeForTax() {
		return incomeForTax;
	}

	public void setIncomeForTax(String incomeForTax) {
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

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTaxDeduction() {
		return taxDeduction;
	}

	public void setTaxDeduction(String taxDeduction) {
		this.taxDeduction = taxDeduction;
	}

	public String getTaxWithholdAmount() {
		return taxWithholdAmount;
	}

	public void setTaxWithholdAmount(String taxWithholdAmount) {
		this.taxWithholdAmount = taxWithholdAmount;
	}

	public String getTaxWithholdedAmount() {
		return taxWithholdedAmount;
	}

	public void setTaxWithholdedAmount(String taxWithholdedAmount) {
		this.taxWithholdedAmount = taxWithholdedAmount;
	}

	public String getTaxRemedyOrReturn() {
		return taxRemedyOrReturn;
	}

	public void setTaxRemedyOrReturn(String taxRemedyOrReturn) {
		this.taxRemedyOrReturn = taxRemedyOrReturn;
	}

	public Boolean getDeclare() {
		return isDeclare;
	}

	public void setDeclare(Boolean declare) {
		isDeclare = declare;
	}

	public Boolean getTranfer() {
		return isTranfer;
	}

	public void setTranfer(Boolean tranfer) {
		isTranfer = tranfer;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean pay) {
		isPay = pay;
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

	public String getIncomeSubjectName() {
		return incomeSubjectName;
	}

	public void setIncomeSubjectName(String incomeSubjectName) {
		this.incomeSubjectName = incomeSubjectName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CalculationBatchDetail{" +
			"id=" + id +
			", calculationBatchId=" + calculationBatchId +
			", versionNo=" + versionNo +
			", isDefer=" + isDefer +
			", employeeNo=" + employeeNo +
			", employeeName=" + employeeName +
			", idType=" + idType +
			", idNo=" + idNo +
			", declareAccount=" + declareAccount +
			", payAccount=" + payAccount +
			", period=" + period +
			", incomeSubject=" + incomeSubject +
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
			", taxRate=" + taxRate +
			", quickCalDeduct=" + quickCalDeduct +
			", taxAmount=" + taxAmount +
			", taxDeduction=" + taxDeduction +
			", taxWithholdAmount=" + taxWithholdAmount +
			", taxWithholdedAmount=" + taxWithholdedAmount +
			", taxRemedyOrReturn=" + taxRemedyOrReturn +
			", isDeclare=" + isDeclare +
			", isTranfer=" + isTranfer +
			", isPay=" + isPay +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			"}";
	}
}
