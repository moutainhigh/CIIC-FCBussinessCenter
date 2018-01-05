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
 * 薪资组模板版本历史记录表
 * </p>
 *
 * @author Neo Jiang
 * @since 2018-01-04
 */
@TableName("pr_payroll_group_template_history")
public class PrPayrollGroupTemplateHistoryPO extends Model<PrPayrollGroupTemplateHistoryPO> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 薪资组模板code
     */
	@TableField("payroll_group_template_code")
	private String payrollGroupTemplateCode;
    /**
     * 薪资组模板版本
     */
	private String version;
    /**
     * 该薪资组模板版本的详细数据
     */
	@TableField("payroll_group_template_history")
	private String payrollGroupTemplateHistory;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayrollGroupTemplateCode() {
		return payrollGroupTemplateCode;
	}

	public void setPayrollGroupTemplateCode(String payrollGroupTemplateCode) {
		this.payrollGroupTemplateCode = payrollGroupTemplateCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPayrollGroupTemplateHistory() {
		return payrollGroupTemplateHistory;
	}

	public void setPayrollGroupTemplateHistory(String payrollGroupTemplateHistory) {
		this.payrollGroupTemplateHistory = payrollGroupTemplateHistory;
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
		return "PrPayrollGroupTemplateHistoryPO{" +
			"id=" + id +
			", payrollGroupTemplateCode=" + payrollGroupTemplateCode +
			", version=" + version +
			", payrollGroupTemplateHistory=" + payrollGroupTemplateHistory +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
