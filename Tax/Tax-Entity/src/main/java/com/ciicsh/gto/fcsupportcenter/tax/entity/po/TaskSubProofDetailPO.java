package com.ciicsh.gto.fcsupportcenter.tax.entity.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 完税申请明细
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-07
 */
@TableName("tax_fc_task_sub_proof_detail")
public class TaskSubProofDetailPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 完税凭证子任务ID
     */
	@TableField("task_sub_proof_id")
	private Long taskSubProofId;
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
     * 所得项目
     */
	@TableField("income_subject")
	private String incomeSubject;
    /**
     * 所得期间起
     */
	@TableField("income_start")
	private LocalDate incomeStart;
    /**
     * 所得期间止
     */
	@TableField("income_end")
	private LocalDate incomeEnd;
    /**
     * 应纳税所得额
     */
	@TableField("income_for_tax")
	private BigDecimal incomeForTax;
    /**
     * 扣缴税额
     */
	@TableField("withholded_amount")
	private BigDecimal withholdedAmount;
    /**
     * 是否可用
     */
	@TableField("is_active")
	private Boolean isActive;
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
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 修改人
     */
	@TableField("modified_by")
	private String modifiedBy;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskSubProofId() {
		return taskSubProofId;
	}

	public void setTaskSubProofId(Long taskSubProofId) {
		this.taskSubProofId = taskSubProofId;
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

	public String getIncomeSubject() {
		return incomeSubject;
	}

	public void setIncomeSubject(String incomeSubject) {
		this.incomeSubject = incomeSubject;
	}

	public LocalDate getIncomeStart() {
		return incomeStart;
	}

	public void setIncomeStart(LocalDate incomeStart) {
		this.incomeStart = incomeStart;
	}

	public LocalDate getIncomeEnd() {
		return incomeEnd;
	}

	public void setIncomeEnd(LocalDate incomeEnd) {
		this.incomeEnd = incomeEnd;
	}

	public BigDecimal getIncomeForTax() {
		return incomeForTax;
	}

	public void setIncomeForTax(BigDecimal incomeForTax) {
		this.incomeForTax = incomeForTax;
	}

	public BigDecimal getWithholdedAmount() {
		return withholdedAmount;
	}

	public void setWithholdedAmount(BigDecimal withholdedAmount) {
		this.withholdedAmount = withholdedAmount;
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

	@Override
	public String toString() {
		return "TaskSubProofDetailPO{" +
			"id=" + id +
			", taskSubProofId=" + taskSubProofId +
			", employeeNo=" + employeeNo +
			", employeeName=" + employeeName +
			", idType=" + idType +
			", idNo=" + idNo +
			", declareAccount=" + declareAccount +
			", incomeSubject=" + incomeSubject +
			", incomeStart=" + incomeStart +
			", incomeEnd=" + incomeEnd +
			", incomeForTax=" + incomeForTax +
			", withholdedAmount=" + withholdedAmount +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
