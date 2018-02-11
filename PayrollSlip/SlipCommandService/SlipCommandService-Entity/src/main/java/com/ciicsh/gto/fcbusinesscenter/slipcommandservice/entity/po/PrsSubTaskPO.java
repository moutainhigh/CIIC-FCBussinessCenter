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
 * 工资单子任务单主表
 *
 * @author taka
 * @since 2018-02-11
 */
@TableName("prs_sub_task")
public class PrsSubTaskPO extends Model<PrsSubTaskPO> {

  private static final long serialVersionUID = 1L;

  /**
   * 子任务单ID
   */
  @TableId("sub_task_id")
  private String subTaskId;

  /**
   * 任务单名称
   */
  @TableField("task_title")
  private String taskTitle;

  /**
   * 任务单编号
   */
  @TableField("main_task_id")
  private String mainTaskId;

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
   * 实际发布日期
   */
  @TableField("publish_date")
  private Date publishDate;

  /**
   * 工资单类型:0-通用，1-纸质，2-邮件，3-网上查看
   */
  @TableField("payroll_type")
  private Integer payrollType;

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
   * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效
   */
  @TableField("status")
  private Integer status;

  /**
   * 审核意见
   */
  @TableField("comments")
  private String comments;

  /**
   * 工资单模板Code
   */
  @TableField("template_code")
  private String templateCode;

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



  public String getSubTaskId() {
    return subTaskId;
  }

  public void setSubTaskId(String subTaskId) {
    this.subTaskId = subTaskId;
  }

  public String getTaskTitle() {
    return taskTitle;
  }

  public void setTaskTitle(String taskTitle) {
    this.taskTitle = taskTitle;
  }

  public String getMainTaskId() {
    return mainTaskId;
  }

  public void setMainTaskId(String mainTaskId) {
    this.mainTaskId = mainTaskId;
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

  public Integer getPayrollType() {
    return payrollType;
  }

  public void setPayrollType(Integer payrollType) {
    this.payrollType = payrollType;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(String templateCode) {
    this.templateCode = templateCode;
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
    return this.subTaskId;
  }

  @Override
  public String toString() {
    return "PrsSubTaskPO{" + 
			"subTaskId=" + subTaskId +
			", taskTitle=" + taskTitle +
			", mainTaskId=" + mainTaskId +
			", managementId=" + managementId +
			", managementName=" + managementName +
			", batchId=" + batchId +
			", period=" + period +
			", totalCount=" + totalCount +
			", chineseCount=" + chineseCount +
			", foreignerCount=" + foreignerCount +
			", publishDate=" + publishDate +
			", payrollType=" + payrollType +
			", remark=" + remark +
			", approver=" + approver +
			", approveTime=" + approveTime +
			", approveRemark=" + approveRemark +
			", status=" + status +
			", comments=" + comments +
			", templateCode=" + templateCode +
			", isActive=" + isActive +
			", createdBy=" + createdBy +
			", createdTime=" + createdTime +
			", modifiedBy=" + modifiedBy +
			", modifiedTime=" + modifiedTime +
			"}";
  }
}
