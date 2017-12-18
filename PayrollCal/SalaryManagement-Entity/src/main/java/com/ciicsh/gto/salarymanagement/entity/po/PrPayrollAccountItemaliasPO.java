package com.ciicsh.gto.salarymanagement.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 薪资账套薪资项别名表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_payroll_account_itemalias")
public class PrPayrollAccountItemaliasPO extends Model<PrPayrollAccountItemaliasPO> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 薪资账套Id
     */
	@TableField("payroll_account_id")
	private Integer payrollAccountId;
    /**
     * 薪资项ID
     */
	@TableField("payroll_item_id")
	private Integer payrollItemId;
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
     * 是否显示
     */
	@TableField("is_show")
	private Boolean isShow;
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

	public Integer getPayrollAccountId() {
		return payrollAccountId;
	}

	public void setPayrollAccountId(Integer payrollAccountId) {
		this.payrollAccountId = payrollAccountId;
	}

	public Integer getPayrollItemId() {
		return payrollItemId;
	}

	public void setPayrollItemId(Integer payrollItemId) {
		this.payrollItemId = payrollItemId;
	}

	public String getPayrollItemAlias() {
		return payrollItemAlias;
	}

	public void setPayrollItemAlias(String payrollItemAlias) {
		this.payrollItemAlias = payrollItemAlias;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getShow() {
		return isShow;
	}

	public void setShow(Boolean isShow) {
		this.isShow = isShow;
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
		return "PrPayrollAccountItemaliasPO{" +
			"id=" + id +
			", payrollAccountId=" + payrollAccountId +
			", payrollItemId=" + payrollItemId +
			", payrollItemAlias=" + payrollItemAlias +
			", isActive=" + isActive +
			", isShow=" + isShow +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
