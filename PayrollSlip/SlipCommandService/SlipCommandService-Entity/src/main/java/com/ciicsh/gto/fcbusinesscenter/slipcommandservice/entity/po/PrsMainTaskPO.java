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
 * 工资单任务单主表
 *
 * @author taka
 * @since 2018-06-04
 */
@TableName("prs_main_task")
public class PrsMainTaskPO extends Model<PrsMainTaskPO> {

  private static final long serialVersionUID = 1L;

  /**
   * 任务单编号
   */
  @TableId("main_task_id")
  private String mainTaskId;

  /**
   * 任务单名称
   */
  @TableField("title")
  private String title;

  /**
   * 管理方编号
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
   * 雇员列表
   */
  @TableField("employees")
  private String employees;

  /**
   * 总人数
   */
  @TableField("total_count")
  private Integer totalCount;

  /**
   * 中方发薪人数
   */
  @TableField("chinese_count")
  private Integer chineseCount;

  /**
   * 外方发薪人数
   */
  @TableField("foreigner_count")
  private Integer foreignerCount;

  /**
   * 邮件发送日期
   */
  @TableField("publish_date")
  private Date publishDate;

  /**
   * 邮件发送执行日期
   */
  @TableField("publish_exec_date")
  private Date publishExecDate;

  /**
   * 主动发送日期
   */
  @TableField("publish_manual_date")
  private Date publishManualDate;

  /**
   * 上传智翔通日期
   */
  @TableField("upload_date")
  private Date uploadDate;

  /**
   * 上传智翔通执行日期
   */
  @TableField("upload_exec_date")
  private Date uploadExecDate;

  /**
   * 主动发送备注
   */
  @TableField("publish_manual_remark")
  private String publishManualRemark;

  /**
   * 邮件发送状态 0: 未发送 1: 待发送 2: 发送中 3: 发送成功 4: 发送失败 5: 已忽略 6: 无关
   */
  @TableField("publish_state")
  private Integer publishState;

  /**
   * 邮件发送失败日志
   */
  @TableField("publish_fail_log")
  private String publishFailLog;

  /**
   * 上传状态 0: 未上传 1: 待上传 2: 上传中 3: 上传成功 4: 上传失败 5: 已忽略 6: 无关
   */
  @TableField("upload_state")
  private Integer uploadState;

  /**
   * 工资单类型:0-通用，1-纸质，2-邮件，3-网上查看
   */
  @TableField("payroll_type")
  private Integer payrollType;

  /**
   * 选择的工资单类型，逗号分隔
   */
  @TableField("selected_payroll_type")
  private String selectedPayrollType;

  /**
   * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效
   */
  @TableField("status")
  private Integer status;

  /**
   * 是否含纸质
   */
  @TableField("has_paper")
  private Integer hasPaper;

  /**
   * 修改实际发布日期原因
   */
  @TableField("comments")
  private String comments;

  /**
   * 工资单模板id
   */
  @TableField("template_id")
  private Long templateId;

  /**
   * 模板名称
   */
  @TableField("template_name")
  private String templateName;

  /**
   * 备注
   */
  @TableField("remark")
  private String remark;

  /**
   * 审批人
   */
  @TableField("approver")
  private String approver;

  /**
   * 审批时间
   */
  @TableField("approve_time")
  private Date approveTime;

  /**
   * 审批备注
   */
  @TableField("approve_remark")
  private String approveRemark;

  /**
   * 是否有效:1-有效，0-无效
   */
  @TableField("is_active")
  private Boolean isActive;

  /**
   * 创建人
   */
  @TableField("created_by")
  private String createdBy;

  /**
   * 创建时间
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



  public String getMainTaskId() {
    return mainTaskId;
  }

  public void setMainTaskId(String mainTaskId) {
    this.mainTaskId = mainTaskId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getEmployees() {
    return employees;
  }

  public void setEmployees(String employees) {
    this.employees = employees;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getChineseCount() {
    return chineseCount;
  }

  public void setChineseCount(Integer chineseCount) {
    this.chineseCount = chineseCount;
  }

  public Integer getForeignerCount() {
    return foreignerCount;
  }

  public void setForeignerCount(Integer foreignerCount) {
    this.foreignerCount = foreignerCount;
  }

  public Date getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(Date publishDate) {
    this.publishDate = publishDate;
  }

  public Date getPublishExecDate() {
    return publishExecDate;
  }

  public void setPublishExecDate(Date publishExecDate) {
    this.publishExecDate = publishExecDate;
  }

  public Date getPublishManualDate() {
    return publishManualDate;
  }

  public void setPublishManualDate(Date publishManualDate) {
    this.publishManualDate = publishManualDate;
  }

  public Date getUploadDate() {
    return uploadDate;
  }

  public void setUploadDate(Date uploadDate) {
    this.uploadDate = uploadDate;
  }

  public Date getUploadExecDate() {
    return uploadExecDate;
  }

  public void setUploadExecDate(Date uploadExecDate) {
    this.uploadExecDate = uploadExecDate;
  }

  public String getPublishManualRemark() {
    return publishManualRemark;
  }

  public void setPublishManualRemark(String publishManualRemark) {
    this.publishManualRemark = publishManualRemark;
  }

  public Integer getPublishState() {
    return publishState;
  }

  public void setPublishState(Integer publishState) {
    this.publishState = publishState;
  }

  public String getPublishFailLog() {
    return publishFailLog;
  }

  public void setPublishFailLog(String publishFailLog) {
    this.publishFailLog = publishFailLog;
  }

  public Integer getUploadState() {
    return uploadState;
  }

  public void setUploadState(Integer uploadState) {
    this.uploadState = uploadState;
  }

  public Integer getPayrollType() {
    return payrollType;
  }

  public void setPayrollType(Integer payrollType) {
    this.payrollType = payrollType;
  }

  public String getSelectedPayrollType() {
    return selectedPayrollType;
  }

  public void setSelectedPayrollType(String selectedPayrollType) {
    this.selectedPayrollType = selectedPayrollType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getHasPaper() {
    return hasPaper;
  }

  public void setHasPaper(Integer hasPaper) {
    this.hasPaper = hasPaper;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getApprover() {
    return approver;
  }

  public void setApprover(String approver) {
    this.approver = approver;
  }

  public Date getApproveTime() {
    return approveTime;
  }

  public void setApproveTime(Date approveTime) {
    this.approveTime = approveTime;
  }

  public String getApproveRemark() {
    return approveRemark;
  }

  public void setApproveRemark(String approveRemark) {
    this.approveRemark = approveRemark;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
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
    return this.mainTaskId;
  }

  @Override
  public String toString() {
    return "PrsMainTaskPO{" +
            "mainTaskId=" + mainTaskId +
            ", title=" + title +
            ", managementId=" + managementId +
            ", managementName=" + managementName +
            ", batchId=" + batchId +
            ", period=" + period +
            ", personnelIncomeYearMonth=" + personnelIncomeYearMonth +
            ", employees=" + employees +
            ", totalCount=" + totalCount +
            ", chineseCount=" + chineseCount +
            ", foreignerCount=" + foreignerCount +
            ", publishDate=" + publishDate +
            ", publishExecDate=" + publishExecDate +
            ", publishManualDate=" + publishManualDate +
            ", uploadDate=" + uploadDate +
            ", uploadExecDate=" + uploadExecDate +
            ", publishManualRemark=" + publishManualRemark +
            ", publishState=" + publishState +
            ", publishFailLog=" + publishFailLog +
            ", uploadState=" + uploadState +
            ", payrollType=" + payrollType +
            ", selectedPayrollType=" + selectedPayrollType +
            ", status=" + status +
            ", hasPaper=" + hasPaper +
            ", comments=" + comments +
            ", templateId=" + templateId +
            ", templateName=" + templateName +
            ", remark=" + remark +
            ", approver=" + approver +
            ", approveTime=" + approveTime +
            ", approveRemark=" + approveRemark +
            ", isActive=" + isActive +
            ", createdBy=" + createdBy +
            ", createdTime=" + createdTime +
            ", modifiedBy=" + modifiedBy +
            ", modifiedTime=" + modifiedTime +
            "}";
  }
}
