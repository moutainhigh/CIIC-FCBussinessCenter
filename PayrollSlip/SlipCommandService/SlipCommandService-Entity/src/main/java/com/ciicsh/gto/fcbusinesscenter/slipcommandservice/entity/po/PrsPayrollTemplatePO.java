package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;


/**
 * 工资单模板
 *
 * @author taka
 * @since 2018-01-30
 */
@TableName("prs_payroll_template")
public class PrsPayrollTemplatePO extends Model<PrsPayrollTemplatePO> {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @TableId(value="id", type=IdType.AUTO)
  private Long id;

  /**
   * 模板Code
   */
  @TableField("template_code")
  private String templateCode;

  /**
   * 模板名称
   */
  @TableField("template_name")
  private String templateName;

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
   * 薪资账套code
   */
  @TableField("account_code")
  private String accountCode;

  /**
   * 薪资账套名称
   */
  @TableField("account_name")
  private String accountName;

  /**
   * 薪资项json
   */
  @TableField("account_items")
  private String accountItems;

  /**
   * 模板类型：0：通用 1：纸质 2：邮件 3：网上查看
   */
  @TableField("template_type")
  private Integer templateType;

  /**
   * 模板文件名称
   */
  @TableField("template_file_name")
  private String templateFileName;

  /**
   * 模板文件上传url
   */
  @TableField("template_file_url")
  private String templateFileUrl;

  /**
   * 生效时间
   */
  @TableField("effective_time")
  private Date effectiveTime;

  /**
   * 失效时间
   */
  @TableField("invalid_time")
  private Date invalidTime;

  /**
   * 是否加密
   */
  @TableField("if_encrypt")
  private Boolean ifEncrypt;

  /**
   * 状态（0：草稿 1:）
   */
  @TableField("status")
  private Integer status;

  /**
   * 审核意见
   */
  @TableField("comments")
  private String comments;

  /**
   * 备注
   */
  @TableField("remark")
  private String remark;

  /**
   * 是否禁用
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

  public String getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(String templateCode) {
    this.templateCode = templateCode;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
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

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getAccountItems() {
    return accountItems;
  }

  public void setAccountItems(String accountItems) {
    this.accountItems = accountItems;
  }

  public Integer getTemplateType() {
    return templateType;
  }

  public void setTemplateType(Integer templateType) {
    this.templateType = templateType;
  }

  public String getTemplateFileName() {
    return templateFileName;
  }

  public void setTemplateFileName(String templateFileName) {
    this.templateFileName = templateFileName;
  }

  public String getTemplateFileUrl() {
    return templateFileUrl;
  }

  public void setTemplateFileUrl(String templateFileUrl) {
    this.templateFileUrl = templateFileUrl;
  }

  public Date getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(Date effectiveTime) {
    this.effectiveTime = effectiveTime;
  }

  public Date getInvalidTime() {
    return invalidTime;
  }

  public void setInvalidTime(Date invalidTime) {
    this.invalidTime = invalidTime;
  }

  public Boolean getIfEncrypt() {
    return ifEncrypt;
  }

  public void setIfEncrypt(Boolean ifEncrypt) {
    this.ifEncrypt = ifEncrypt;
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
    return "PrsPayrollTemplatePO{" +
            "id=" + id +
            ", templateCode=" + templateCode +
            ", templateName=" + templateName +
            ", managementId=" + managementId +
            ", managementName=" + managementName +
            ", accountCode=" + accountCode +
            ", accountName=" + accountName +
            ", accountItems=" + accountItems +
            ", templateType=" + templateType +
            ", templateFileName=" + templateFileName +
            ", templateFileUrl=" + templateFileUrl +
            ", effectiveTime=" + effectiveTime +
            ", invalidTime=" + invalidTime +
            ", ifEncrypt=" + ifEncrypt +
            ", status=" + status +
            ", comments=" + comments +
            ", remark=" + remark +
            ", isActive=" + isActive +
            ", createdTime=" + createdTime +
            ", modifiedTime=" + modifiedTime +
            ", createdBy=" + createdBy +
            ", modifiedBy=" + modifiedBy +
            "}";
  }
}
