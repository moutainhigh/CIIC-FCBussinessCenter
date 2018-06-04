package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 已发布的工资单
 *
 * @author taka
 * @since 2018-06-04
 */
@TableName("prs_payroll")
public class PrsPayrollPO extends Model<PrsPayrollPO> {

  private static final long serialVersionUID = 1L;

  /**
   * 工资单ID
   */
  @TableId(value="id", type=IdType.AUTO)
  private Long id;

  /**
   * 工资单编号
   */
  @TableField("payroll_code")
  private String payrollCode;

  /**
   * 管理方ID
   */
  @TableField("management_id")
  private String managementId;

  /**
   *
   */
  @TableField("management_name")
  private String managementName;

  /**
   * 雇员ID
   */
  @TableField("employee_id")
  private String employeeId;

  /**
   * 雇员ID
   */
  @TableField("employee_name")
  private String employeeName;

  /**
   * 实发工资
   */
  @TableField("net_pay")
  private String netPay;

  /**
   * 薪资周期
   */
  @TableField("period")
  private String period;

  /**
   * 工资年月
   */
  @TableField("personnel_income_year_month")
  private String personnelIncomeYearMonth;

  /**
   * 工资单批次
   */
  @TableField("batch_id")
  private String batchId;

  /**
   * 模板id
   */
  @TableField("template_id")
  private Long templateId;

  /**
   * 模板名称
   */
  @TableField("template_name")
  private String templateName;

  /**
   * 渠道：1. 外部  2：薪酬计算  3：合并
   */
  @TableField("channel")
  private Integer channel;

  /**
   * 薪资项
   */
  @TableField("items")
  private String items;

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



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPayrollCode() {
    return payrollCode;
  }

  public void setPayrollCode(String payrollCode) {
    this.payrollCode = payrollCode;
  }

  public String getManagementId() {
    return managementId;
  }

  public void setManagementId(String managementId) {
    this.managementId = managementId;
  }

  public String getManagementName() {
    return managementName;
  }

  public void setManagementName(String managementName) {
    this.managementName = managementName;
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

  public String getNetPay() {
    return netPay;
  }

  public void setNetPay(String netPay) {
    this.netPay = netPay;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public String getPersonnelIncomeYearMonth() {
    return personnelIncomeYearMonth;
  }

  public void setPersonnelIncomeYearMonth(String personnelIncomeYearMonth) {
    this.personnelIncomeYearMonth = personnelIncomeYearMonth;
  }

  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  public Long getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Long templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public Integer getChannel() {
    return channel;
  }

  public void setChannel(Integer channel) {
    this.channel = channel;
  }

  public String getItems() {
    return items;
  }

  public void setItems(String items) {
    this.items = items;
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
    return this.id;
  }

  @Override
  public String toString() {
    return "PrsPayrollPO{" +
            "id=" + id +
            ", payrollCode=" + payrollCode +
            ", managementId=" + managementId +
            ", managementName=" + managementName +
            ", employeeId=" + employeeId +
            ", employeeName=" + employeeName +
            ", netPay=" + netPay +
            ", period=" + period +
            ", personnelIncomeYearMonth=" + personnelIncomeYearMonth +
            ", batchId=" + batchId +
            ", templateId=" + templateId +
            ", templateName=" + templateName +
            ", channel=" + channel +
            ", items=" + items +
            ", remark=" + remark +
            ", isActive=" + isActive +
            ", createdTime=" + createdTime +
            ", modifiedTime=" + modifiedTime +
            ", createdBy=" + createdBy +
            ", modifiedBy=" + modifiedBy +
            "}";
  }
}
