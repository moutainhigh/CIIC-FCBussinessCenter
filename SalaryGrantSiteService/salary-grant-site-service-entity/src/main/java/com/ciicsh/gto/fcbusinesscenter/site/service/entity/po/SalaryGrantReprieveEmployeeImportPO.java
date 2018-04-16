package com.ciicsh.gto.fcbusinesscenter.site.service.entity.po;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 薪资发放暂缓名单导入表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@TableName("sg_salary_grant_reprieve_employee_import")
public class SalaryGrantReprieveEmployeeImportPO extends Model<SalaryGrantReprieveEmployeeImportPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 薪资发放暂缓名单编号
     */
    @TableId("salarygrant_reprieve_employee_import_id")
	private Long salarygrantReprieveEmployeeImportId;
    /**
     * 任务单编号
     */
	@TableField("task_code")
	private String taskCode;
    /**
     * 任务单类型:0-主表、1-中智大库、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
     */
	@TableField("task_type")
	private Integer taskType;
    /**
     * 雇员编号
     */
	@TableField("employee_id")
	private String employeeId;
    /**
     * 雇员名称
     */
	@TableField("employee_name")
	private String employeeName;
    /**
     * 导入时间
     */
	@TableField("import_time")
	private Date importTime;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 最后修改时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 最后修改人
     */
	@TableField("modified_by")
	private String modifiedBy;
    /**
     * 最后修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;


	public Long getSalarygrantReprieveEmployeeImportId() {
		return salarygrantReprieveEmployeeImportId;
	}

	public void setSalarygrantReprieveEmployeeImportId(Long salarygrantReprieveEmployeeImportId) {
		this.salarygrantReprieveEmployeeImportId = salarygrantReprieveEmployeeImportId;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.salarygrantReprieveEmployeeImportId;
	}

	@Override
	public String toString() {
		return "SalaryGrantReprieveEmployeeImportPO{" +
			", salarygrantReprieveEmployeeImportId=" + salarygrantReprieveEmployeeImportId +
			", taskCode=" + taskCode +
			", taskType=" + taskType +
			", employeeId=" + employeeId +
			", employeeName=" + employeeName +
			", importTime=" + importTime +
			", createdBy=" + createdBy +
			", createdTime=" + createdTime +
			", modifiedBy=" + modifiedBy +
			", modifiedTime=" + modifiedTime +
			"}";
	}
}
