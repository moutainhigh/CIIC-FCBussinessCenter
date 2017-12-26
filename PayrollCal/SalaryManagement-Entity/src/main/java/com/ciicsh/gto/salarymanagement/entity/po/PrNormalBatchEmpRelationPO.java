package com.ciicsh.gto.salarymanagement.entity.po;

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
 * 正常批次和雇员关系表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_normal_batch_emp_relation")
public class PrNormalBatchEmpRelationPO extends Model<PrNormalBatchEmpRelationPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 正常批次ID
     */
	@TableField("normal_batch_id")
	private Integer normalBatchId;
    /**
     * 雇员ID
     */
	@TableField("emp_id")
	private String empId;
    /**
     * 当前批次中此雇员的实发工资
     */
	@TableField("net_pay")
	private BigDecimal netPay;
    /**
     * 当前批次中此雇员的个调税金额
     */
	@TableField("income_tax")
	private BigDecimal incomeTax;
    /**
     * 当前批次中，此雇员的实发工资调整金额
     */
	@TableField("net_pay_adjust_amount")
	private BigDecimal netPayAdjustAmount;
    /**
     * 当前批次中，此雇员的个调税调整金额
     */
	@TableField("income_tax_adjust_amount")
	private BigDecimal incomeTaxAdjustAmount;
    /**
     * 当前批次中，此雇员的实发工资回溯调整金额
     */
	@TableField("net_pay_Badjust_amount")
	private BigDecimal netPayBadjustAmount;
    /**
     * 是否有效
     */
	@TableField("is_active")
	private Boolean isActive;
    /**
     * 数据创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 最后修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 最后修改人
     */
	@TableField("modified_by")
	private String modifiedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNormalBatchId() {
		return normalBatchId;
	}

	public void setNormalBatchId(Integer normalBatchId) {
		this.normalBatchId = normalBatchId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public BigDecimal getNetPay() {
		return netPay;
	}

	public void setNetPay(BigDecimal netPay) {
		this.netPay = netPay;
	}

	public BigDecimal getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(BigDecimal incomeTax) {
		this.incomeTax = incomeTax;
	}

	public BigDecimal getNetPayAdjustAmount() {
		return netPayAdjustAmount;
	}

	public void setNetPayAdjustAmount(BigDecimal netPayAdjustAmount) {
		this.netPayAdjustAmount = netPayAdjustAmount;
	}

	public BigDecimal getIncomeTaxAdjustAmount() {
		return incomeTaxAdjustAmount;
	}

	public void setIncomeTaxAdjustAmount(BigDecimal incomeTaxAdjustAmount) {
		this.incomeTaxAdjustAmount = incomeTaxAdjustAmount;
	}

	public BigDecimal getNetPayBadjustAmount() {
		return netPayBadjustAmount;
	}

	public void setNetPayBadjustAmount(BigDecimal netPayBadjustAmount) {
		this.netPayBadjustAmount = netPayBadjustAmount;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PrNormalBatchEmpRelationPO{" +
			"id=" + id +
			", normalBatchId=" + normalBatchId +
			", empId=" + empId +
			", netPay=" + netPay +
			", incomeTax=" + incomeTax +
			", netPayAdjustAmount=" + netPayAdjustAmount +
			", incomeTaxAdjustAmount=" + incomeTaxAdjustAmount +
			", netPayBadjustAmount=" + netPayBadjustAmount +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
