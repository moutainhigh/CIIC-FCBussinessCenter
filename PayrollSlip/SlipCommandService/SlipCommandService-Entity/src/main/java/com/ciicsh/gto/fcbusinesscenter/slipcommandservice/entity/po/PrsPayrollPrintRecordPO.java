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
 * 工资单任务单打印记录
 *
 * @author taka
 * @since 2018-02-24
 */
@TableName("prs_payroll_print_record")
public class PrsPayrollPrintRecordPO extends Model<PrsPayrollPrintRecordPO> {

  private static final long serialVersionUID = 1L;

  /**
   * 打印记录ID
   */
  @TableId(value="id", type=IdType.AUTO)
  private Long id;

  /**
   * 任务单编号
   */
  @TableField("task_id")
  private String taskId;

  /**
   * 任务单标题
   */
  @TableField("task_title")
  private String taskTitle;

  /**
   * 管理方ID
   */
  @TableField("management_id")
  private String managementId;

  /**
   * 管理方名称
   */
  @TableField("management_name")
  private String managementName;

  /**
   * 薪酬计算批次号
   */
  @TableField("batch_id")
  private String batchId;

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
   * 打印文件
   */
  @TableField("print_file")
  private String printFile;

  /**
   * 状态  1：未打印  2：已打印
   */
  @TableField("status")
  private Integer status;

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

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getTaskTitle() {
    return taskTitle;
  }

  public void setTaskTitle(String taskTitle) {
    this.taskTitle = taskTitle;
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

  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
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

  public String getPrintFile() {
    return printFile;
  }

  public void setPrintFile(String printFile) {
    this.printFile = printFile;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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
    return "PrsPayrollPrintRecordPO{" +
            "id=" + id +
            ", taskId=" + taskId +
            ", taskTitle=" + taskTitle +
            ", managementId=" + managementId +
            ", managementName=" + managementName +
            ", batchId=" + batchId +
            ", period=" + period +
            ", personnelIncomeYearMonth=" + personnelIncomeYearMonth +
            ", printFile=" + printFile +
            ", status=" + status +
            ", remark=" + remark +
            ", isActive=" + isActive +
            ", createdTime=" + createdTime +
            ", modifiedTime=" + modifiedTime +
            ", createdBy=" + createdBy +
            ", modifiedBy=" + modifiedBy +
            "}";
  }
}
