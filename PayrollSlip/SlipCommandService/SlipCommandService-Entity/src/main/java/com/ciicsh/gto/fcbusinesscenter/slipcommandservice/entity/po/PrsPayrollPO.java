package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;


/**
 * 工资单
 *
 * @author taka
 * @since 2018-02-09
 */
@TableName("prs_payroll")
public class PrsPayrollPO extends Model<PrsPayrollPO> {

  private static final long serialVersionUID = 1L;

  /**
   * 工资单ID
   */
  @TableId(value="payroll_id", type=IdType.AUTO)
  private Long payrollId;

  /**
   * 管理方ID
   */
  @TableField("management_id")
  private String managementId;

  /**
   * 雇员ID
   */
  @TableField("employee_id")
  private String employeeId;

  /**
   * 薪资周期
   */
  @TableField("salary_period")
  private String salaryPeriod;

  /**
   * 工资单批次
   */
  @TableField("payroll_batch")
  private String payrollBatch;

  /**
   * 渠道：1. 外部  2：薪酬计算  3：合并
   */
  @TableField("channel")
  private Integer channel;

  /**
   * 数据对象\r\n[    \r\n{"Key":"","Value":"","ShowTitle":"","DataType":"","Note":""},    {"Key":"","Value":"","ShowTitle":"","DataType":"","Note":""},    {"Key":"","Value":"","ShowTitle":"","DataType":"","Note":""}\r\n]
   */
  @TableField("data_object")
  private String dataObject;

  /**
   * 备注
   */
  @TableField("remark")
  private String remark;

  /**
   * 是否可用
   */
  @TableField("is_active")
  private Boolean isActive;

  /**
   * 创建时间
   */
  @TableField("created_time")
  private Date createdTime;

  /**
   * 最后更新时间
   */
  @TableField("modified_time")
  private Date modifiedTime;

  /**
   * 创建者登录名
   */
  @TableField("created_by")
  private String createdBy;

  /**
   * 修改者登录名
   */
  @TableField("modified_by")
  private String modifiedBy;



  public Long getPayrollId() {
    return payrollId;
  }

  public void setPayrollId(Long payrollId) {
    this.payrollId = payrollId;
  }

  public String getManagementId() {
    return managementId;
  }

  public void setManagementId(String managementId) {
    this.managementId = managementId;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public String getSalaryPeriod() {
    return salaryPeriod;
  }

  public void setSalaryPeriod(String salaryPeriod) {
    this.salaryPeriod = salaryPeriod;
  }

  public String getPayrollBatch() {
    return payrollBatch;
  }

  public void setPayrollBatch(String payrollBatch) {
    this.payrollBatch = payrollBatch;
  }

  public Integer getChannel() {
    return channel;
  }

  public void setChannel(Integer channel) {
    this.channel = channel;
  }

  public String getDataObject() {
    return dataObject;
  }

  public void setDataObject(String dataObject) {
    this.dataObject = dataObject;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
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
    return this.payrollId;
  }

  @Override
  public String toString() {
    return "PrsPayrollPO{" + 
			"payrollId=" + payrollId +
			", managementId=" + managementId +
			", employeeId=" + employeeId +
			", salaryPeriod=" + salaryPeriod +
			", payrollBatch=" + payrollBatch +
			", channel=" + channel +
			", dataObject=" + dataObject +
			", remark=" + remark +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
  }
}
