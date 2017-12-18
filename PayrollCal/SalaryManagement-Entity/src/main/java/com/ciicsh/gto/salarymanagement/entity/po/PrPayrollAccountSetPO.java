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
 * 配置表，薪酬账套信息
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_payroll_account_set")
public class PrPayrollAccountSetPO extends Model<PrPayrollAccountSetPO> {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增薪资账套ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	/**
	 * 所属管理方ID
	 */
	@TableField("management_id")
	private String managementId;
	/**
	 * 薪酬账套名称
	 */
	@TableField("account_set_name")
	private String accountSetName;
	/**
	 * 薪酬账套代码
	 */
	@TableField("account_set_code")
	private String accountSetCode;
	/**
	 * 是否是薪资组模板
	 */
	@TableField("if_group_template")
	private Boolean ifGroupTemplate;

	/**
	 * 薪资组编码
	 */
	@TableField("payroll_group_code")
	private String payrollGroupCode;
	/**
	 * 薪资组模版编码
	 */
	@TableField("payroll_group_template_code")
	private String payrollGroupTemplateCode;
	/**
	 * 雇员组编码
	 */
	@TableField("emp_group_code")
	private String empGroupCode;
	/**
	 * 工资期间开始日
	 */
	@TableField("start_day")
	private Integer startDay;
	/**
	 * 工资期间结束日
	 */
	@TableField("end_day")
	private Integer endDay;
	/**
	 * 枚举类型：
	 0 表示 本月，
	 1 表示 上月，
	 2 表示 下月
	 */
	@TableField("payroll_period")
	private Integer payrollPeriod;
	/**
	 * 工作日历名称
	 */
	@TableField("work_calendar_name")
	private String workCalendarName;
	/**
	 * 工作日历值
	 */
	@TableField("work_calendar_value")
	private String workCalendarValue;
	/**
	 * 备注
	 */
	private String remark;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public String getAccountSetName() {
		return accountSetName;
	}

	public void setAccountSetName(String accountSetName) {
		this.accountSetName = accountSetName;
	}

	public String getAccountSetCode() {
		return accountSetCode;
	}

	public void setAccountSetCode(String accountSetCode) {
		this.accountSetCode = accountSetCode;
	}

	public String getPayrollGroudCode() {
		return payrollGroupCode;
	}

	public void setPayrollGroudCode(String payrollGroupCode) {
		this.payrollGroupCode = payrollGroupCode;
	}

	public String getPayrollGroudTemplateCode() {
		return payrollGroupTemplateCode;
	}

	public void setPayrollGroudTemplateCode(String payrollGroupTemplateCode) {
		this.payrollGroupTemplateCode = payrollGroupTemplateCode;
	}

	public String getEmpGroupCode() {
		return empGroupCode;
	}

	public void setEmpGroupCode(String empGroupCode) {
		this.empGroupCode = empGroupCode;
	}

	public Integer getStartDay() {
		return startDay;
	}

	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}

	public Integer getEndDay() {
		return endDay;
	}

	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}

	public Integer getPayrollPeriod() {
		return payrollPeriod;
	}

	public void setPayrollPeriod(Integer payrollPeriod) {
		this.payrollPeriod = payrollPeriod;
	}

	public String getWorkCalendarName() {
		return workCalendarName;
	}

	public void setWorkCalendarName(String workCalendarName) {
		this.workCalendarName = workCalendarName;
	}

	public String getWorkCalendarValue() {
		return workCalendarValue;
	}

	public void setWorkCalendarValue(String workCalendarValue) {
		this.workCalendarValue = workCalendarValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return "PrPayrollAccountSet{" +
				"id=" + id +
				", managementId=" + managementId +
				", accountSetName=" + accountSetName +
				", accountSetCode=" + accountSetCode +
				", ifGroupTemplate=" + ifGroupTemplate +
				", payrollGroupCode=" + payrollGroupCode +
				", payrollGroupTemplateCode=" + payrollGroupTemplateCode +
				", empGroupCode=" + empGroupCode +
				", startDay=" + startDay +
				", endDay=" + endDay +
				", payrollPeriod=" + payrollPeriod +
				", workCalendarName=" + workCalendarName +
				", workCalendarValue=" + workCalendarValue +
				", remark=" + remark +
				", isActive=" + isActive +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", createdBy=" + createdBy +
				", modifiedBy=" + modifiedBy +
				"}";
	}
}
