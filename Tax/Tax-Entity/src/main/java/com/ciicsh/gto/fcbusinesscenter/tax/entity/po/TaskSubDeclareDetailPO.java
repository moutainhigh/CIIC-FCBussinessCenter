package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 申报明细
 * </p>
 *
 * @author wuhua
 * @since 2018-01-24
 */
@TableName("tax_fc_task_sub_declare_detail")
public class TaskSubDeclareDetailPO extends Model<TaskSubDeclareDetailPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 申报子任务ID
     */
	private Long taskSubDeclareId;
    /**
     * 计算批次明细ID
     */
	private Long calculationBatchDetailId;
    /**
     * 版本号
     */
	private Integer versionNo;
    /**
     * 合并后申报明细ID（非空，则已合并，记录为合并后的明细ID）
     */
	private Long taskSubDeclareDetailId;
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
	private Date period;
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
	private Date createdTime;
    /**
     * 修改时间
     */
	private Date modifiedTime;
    /**
     * 创建人
     */
	private String createdBy;
    /**
     * 修改人
     */
	private String modifiedBy;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskSubDeclareId() {
		return taskSubDeclareId;
	}

	public void setTaskSubDeclareId(Long taskSubDeclareId) {
		this.taskSubDeclareId = taskSubDeclareId;
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

	public Long getTaskSubDeclareDetailId() {
		return taskSubDeclareDetailId;
	}

	public void setTaskSubDeclareDetailId(Long taskSubDeclareDetailId) {
		this.taskSubDeclareDetailId = taskSubDeclareDetailId;
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

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TaskSubDeclareDetailPO{" +
				"id=" + id +
				", taskSubDeclareId=" + taskSubDeclareId +
				", calculationBatchDetailId=" + calculationBatchDetailId +
				", versionNo=" + versionNo +
				", taskSubDeclareDetailId=" + taskSubDeclareDetailId +
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
				'}';
	}
}
