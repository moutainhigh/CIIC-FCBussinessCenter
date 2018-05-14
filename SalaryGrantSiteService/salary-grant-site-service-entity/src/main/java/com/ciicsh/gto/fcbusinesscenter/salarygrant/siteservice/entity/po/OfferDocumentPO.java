package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 薪资发放报盘信息表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-02
 */
@TableName("sg_offer_document")
public class OfferDocumentPO extends Model<OfferDocumentPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 报盘信息ID
     */
    @TableId(value="offer_document_id", type= IdType.AUTO)
    private Long offerDocumentId;
    /**
     * 任务单编号
     */
    @TableField("task_code")
    private String taskCode;
    /**
     * 银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
     */
    @TableField("bankcard_type")
    private Integer bankcardType;
    /**
     * 公司编号
     */
    @TableField("company_id")
    private String companyId;
    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;
    /**
     * 付款账号
     */
    @TableField("payment_account_code")
    private String paymentAccountCode;
    /**
     * 付款账户名
     */
    @TableField("payment_account_name")
    private String paymentAccountName;
    /**
     * 薪资发放总金额（RMB）
     */
    @TableField("payment_total_sum")
    private BigDecimal paymentTotalSum;
    /**
     * 发薪人数
     */
    @TableField("total_person_count")
    private Integer totalPersonCount;
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


    public Long getOfferDocumentId() {
        return offerDocumentId;
    }

    public void setOfferDocumentId(Long offerDocumentId) {
        this.offerDocumentId = offerDocumentId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Integer getBankcardType() {
        return bankcardType;
    }

    public void setBankcardType(Integer bankcardType) {
        this.bankcardType = bankcardType;
    }

    public BigDecimal getPaymentTotalSum() {
        return paymentTotalSum;
    }

    public void setPaymentTotalSum(BigDecimal paymentTotalSum) {
        this.paymentTotalSum = paymentTotalSum;
    }

    public Integer getTotalPersonCount() {
        return totalPersonCount;
    }

    public void setTotalPersonCount(Integer totalPersonCount) {
        this.totalPersonCount = totalPersonCount;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPaymentAccountCode() {
        return paymentAccountCode;
    }

    public void setPaymentAccountCode(String paymentAccountCode) {
        this.paymentAccountCode = paymentAccountCode;
    }

    public String getPaymentAccountName() {
        return paymentAccountName;
    }

    public void setPaymentAccountName(String paymentAccountName) {
        this.paymentAccountName = paymentAccountName;
    }

    @Override
    protected Serializable pkVal() {
        return this.offerDocumentId;
    }

    @Override
    public String toString() {
        return "OfferDocumentPO{" +
                ", offerDocumentId=" + offerDocumentId +
                ", taskCode=" + taskCode +
                ", bankcardType=" + bankcardType +
                ", companyId=" + companyId +
                ", companyName=" + companyName +
                ", paymentAccountCode=" + paymentAccountCode +
                ", paymentAccountName=" + paymentAccountName +
                ", paymentTotalSum=" + paymentTotalSum +
                ", totalPersonCount=" + totalPersonCount +
                ", isActive=" + isActive +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", modifiedBy=" + modifiedBy +
                ", modifiedTime=" + modifiedTime +
                "}";
    }
}