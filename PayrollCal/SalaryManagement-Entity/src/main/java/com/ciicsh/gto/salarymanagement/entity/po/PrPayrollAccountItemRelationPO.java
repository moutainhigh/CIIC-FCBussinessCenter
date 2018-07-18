package com.ciicsh.gto.salarymanagement.entity.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 薪资账套薪资项名表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-19
 */
@TableName("pr_payroll_account_item_relation")
public class PrPayrollAccountItemRelationPO extends Model<PrPayrollAccountItemRelationPO> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 薪酬账套代码
     */
	@TableField("account_set_code")
	private String accountSetCode;
    /**
     * 薪资项编码, 规则为XZX-9位管理方ID-薪资项类型缩写-3位数字序号
     */
	@TableField("payroll_item_code")
	private String payrollItemCode;

	/**
	 * 薪资组编码，规则为：XZZ-客户ID-xxx(三位数字序号)
	 */
	@TableField("payroll_group_code")

	private String payrollGroupCode;
    /**
     * 薪资账套薪资项别名
     */
	@TableField("payroll_item_alias")
	private String payrollItemAlias;
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

	public String getAccountSetCode() {
		return accountSetCode;
	}

	public void setAccountSetCode(String accountSetCode) {
		this.accountSetCode = accountSetCode;
	}

	public String getPayrollItemCode() {
		return payrollItemCode;
	}

	public void setPayrollItemCode(String payrollItemCode) {
		this.payrollItemCode = payrollItemCode;
	}

	public String getPayrollGroupCode() {
		return payrollGroupCode;
	}

	public void setPayrollGroupCode(String payrollGroupCode) {
		this.payrollGroupCode = payrollGroupCode;
	}

	public String getPayrollItemAlias() {
		return payrollItemAlias;
	}

	public void setPayrollItemAlias(String payrollItemAlias) {
		this.payrollItemAlias = payrollItemAlias;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
		return "PrPayrollAccountItemRelationPO{" +
			"id=" + id +
			", accountSetCode=" + accountSetCode +
			", payrollItemCode=" + payrollItemCode +
			", payrollItemAlias=" + payrollItemAlias +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
