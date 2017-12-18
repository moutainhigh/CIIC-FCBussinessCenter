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
 * 固定项表。记录固定项的名字和值，和雇员雇员ID关联和批次无关。
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_employee_base_item_relation")
public class PrEmployeeBaseItemRelationPO extends Model<PrEmployeeBaseItemRelationPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 正常批次计算结果ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 雇员ID
     */
	@TableField("emp_id")
	private String empId;
    /**
     * 薪资项计算结果
     */
	@TableField("emp_base_value")
	private String empBaseValue;
    /**
     * 基类薪资项Code
     */
	@TableField("base_item_code")
	private String baseItemCode;
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

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpBaseValue() {
		return empBaseValue;
	}

	public void setEmpBaseValue(String empBaseValue) {
		this.empBaseValue = empBaseValue;
	}

	public String getBaseItemCode() {
		return baseItemCode;
	}

	public void setBaseItemCode(String baseItemCode) {
		this.baseItemCode = baseItemCode;
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
		return "PrEmployeeBaseItemRelationPO{" +
			"id=" + id +
			", empId=" + empId +
			", empBaseValue=" + empBaseValue +
			", baseItemCode=" + baseItemCode +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
