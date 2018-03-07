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
 * 工资单邮寄快递信息
 *
 * @author taka
 * @since 2018-02-24
 */
@TableName("prs_payroll_express")
public class PrsPayrollExpressPO extends Model<PrsPayrollExpressPO> {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @TableId(value="id", type=IdType.AUTO)
  private Long id;

  /**
   * 任务单编号
   */
  @TableField("task_id")
  private String taskId;

  /**
   * 收件人
   */
  @TableField("recipient")
  private String recipient;

  /**
   * 收件人地址
   */
  @TableField("receive_address")
  private String receiveAddress;

  /**
   * 邮编
   */
  @TableField("postcode")
  private String postcode;

  /**
   * 收件人类型  1：HR  2：雇员  3：其他
   */
  @TableField("recipient_type")
  private Integer recipientType;

  /**
   * 快递公司
   */
  @TableField("express_company")
  private String expressCompany;

  /**
   * 快递单号
   */
  @TableField("express_number")
  private String expressNumber;

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

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getReceiveAddress() {
    return receiveAddress;
  }

  public void setReceiveAddress(String receiveAddress) {
    this.receiveAddress = receiveAddress;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public Integer getRecipientType() {
    return recipientType;
  }

  public void setRecipientType(Integer recipientType) {
    this.recipientType = recipientType;
  }

  public String getExpressCompany() {
    return expressCompany;
  }

  public void setExpressCompany(String expressCompany) {
    this.expressCompany = expressCompany;
  }

  public String getExpressNumber() {
    return expressNumber;
  }

  public void setExpressNumber(String expressNumber) {
    this.expressNumber = expressNumber;
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
    return "PrsPayrollExpressPO{" + 
			"id=" + id +
			", taskId=" + taskId +
			", recipient=" + recipient +
			", receiveAddress=" + receiveAddress +
			", postcode=" + postcode +
			", recipientType=" + recipientType +
			", expressCompany=" + expressCompany +
			", expressNumber=" + expressNumber +
			", remark=" + remark +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
  }
}
