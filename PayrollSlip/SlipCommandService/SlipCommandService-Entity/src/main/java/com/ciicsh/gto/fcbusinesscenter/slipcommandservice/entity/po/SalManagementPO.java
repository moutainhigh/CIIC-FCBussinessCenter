package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;


/**
 * 管理方
 *
 * @author taka
 * @since 2018-02-05
 */
@TableName("sal_management")
public class SalManagementPO extends Model<SalManagementPO> {

  private static final long serialVersionUID = 1L;

  /**
   * 管理方ID
   */
  @TableId("management_id")
  private String managementId;

  /**
   * 管理方名称
   */
  @TableField("title")
  private String title;

  /**
   * 管理方描述
   */
  @TableField("description")
  private String description;

  /**
   * 公司数量
   */
  @TableField("company_quantity")
  private Integer companyQuantity;

  /**
   * 雇员数量
   */
  @TableField("employee_number")
  private Integer employeeNumber;

  /**
   * 是否全球500强
   */
  @TableField("is_five_hundred")
  private Boolean isFiveHundred;

  /**
   * 其它名称
   */
  @TableField("other_name")
  private String otherName;

  /**
   * 来源 0：未知 1： 自主开发  2： 交叉销售 3： BD分配\n
   */
  @TableField("source")
  private Integer source;

  /**
   * 管理方状态 0:潜在，1:正式，2:冻结，3:黑名单，4:终止\n
   */
  @TableField("status")
  private Integer status;

  /**
   * 是否为渠道方
   */
  @TableField("is_channel")
  private Boolean isChannel;

  /**
   * 【废弃】业务中心 FC
   */
  @TableField("business_center")
  private String businessCenter;

  /**
   * 【废弃】咨询顾问 FC
   */
  @TableField("consultant")
  private String consultant;

  /**
   * 母公司国家
   */
  @TableField("country_code")
  private String countryCode;

  /**
   * 知名产品
   */
  @TableField("famous_product")
  private String famousProduct;

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
   * 创建者
   */
  @TableField("created_by")
  private String createdBy;

  /**
   * 创建时间
   */
  @TableField("created_time")
  private Date createdTime;

  /**
   * 最后修改者
   */
  @TableField("modified_by")
  private String modifiedBy;

  /**
   * 最后修改时间
   */
  @TableField("modified_time")
  private Date modifiedTime;



  public String getManagementId() {
    return managementId;
  }

  public void setManagementId(String managementId) {
    this.managementId = managementId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getCompanyQuantity() {
    return companyQuantity;
  }

  public void setCompanyQuantity(Integer companyQuantity) {
    this.companyQuantity = companyQuantity;
  }

  public Integer getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(Integer employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  public Boolean getIsFiveHundred() {
    return isFiveHundred;
  }

  public void setIsFiveHundred(Boolean isFiveHundred) {
    this.isFiveHundred = isFiveHundred;
  }

  public String getOtherName() {
    return otherName;
  }

  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Boolean getIsChannel() {
    return isChannel;
  }

  public void setIsChannel(Boolean isChannel) {
    this.isChannel = isChannel;
  }

  public String getBusinessCenter() {
    return businessCenter;
  }

  public void setBusinessCenter(String businessCenter) {
    this.businessCenter = businessCenter;
  }

  public String getConsultant() {
    return consultant;
  }

  public void setConsultant(String consultant) {
    this.consultant = consultant;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getFamousProduct() {
    return famousProduct;
  }

  public void setFamousProduct(String famousProduct) {
    this.famousProduct = famousProduct;
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
    return this.managementId;
  }

  @Override
  public String toString() {
    return "SalManagementPO{" +
            "managementId=" + managementId +
            ", title=" + title +
            ", description=" + description +
            ", companyQuantity=" + companyQuantity +
            ", employeeNumber=" + employeeNumber +
            ", isFiveHundred=" + isFiveHundred +
            ", otherName=" + otherName +
            ", source=" + source +
            ", status=" + status +
            ", isChannel=" + isChannel +
            ", businessCenter=" + businessCenter +
            ", consultant=" + consultant +
            ", countryCode=" + countryCode +
            ", famousProduct=" + famousProduct +
            ", remark=" + remark +
            ", isActive=" + isActive +
            ", createdBy=" + createdBy +
            ", createdTime=" + createdTime +
            ", modifiedBy=" + modifiedBy +
            ", modifiedTime=" + modifiedTime +
            "}";
  }
}
