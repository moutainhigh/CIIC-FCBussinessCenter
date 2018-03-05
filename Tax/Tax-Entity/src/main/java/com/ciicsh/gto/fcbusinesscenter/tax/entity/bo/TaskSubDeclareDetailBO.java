package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author wuhua
 * @since 2018-02-28
 */
public class TaskSubDeclareDetailBO{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
     * 证件编号
     */
	private String idNo;
    /**
     * 申报账户
     */
	private String declareAccount;
    /**
     * 个税期间
     */
	private LocalDate period;
    /**
     * 所得项目
     */
	private String incomeSubject;
    /**
     * 应纳税所得额
     */
	private BigDecimal incomeForTax;
    /**
     * 个税总金额
     */
	private BigDecimal taxAmount;
	/**
	 * 是否有完税凭证服务
	 */
	private Boolean isProof;

	public Boolean getProof() {
		return isProof;
	}

	public void setProof(Boolean proof) {
		isProof = proof;
	}

	public BigDecimal getIncomeForTax() {
		return incomeForTax;
	}

	public void setIncomeForTax(BigDecimal incomeForTax) {
		this.incomeForTax = incomeForTax;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

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

}
