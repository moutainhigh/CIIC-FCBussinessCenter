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
 * 薪资发放报盘文件表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-02
 */
@TableName("sg_offer_document_file")
public class OfferDocumentFilePO extends Model<OfferDocumentFilePO> {

    private static final long serialVersionUID = 1L;

    /**
     * 报盘文件ID
     */
    @TableId(value="offer_document_file_id", type= IdType.AUTO)
    private Long offerDocumentFileId;
    /**
     * 报盘信息ID
     */
    @TableField("offer_document_id")
    private Long offerDocumentId;
    /**
     * 报盘文件名称
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 报盘文件地址
     */
    @TableField("file_path")
    private String filePath;
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


    public Long getOfferDocumentFileId() {
        return offerDocumentFileId;
    }

    public void setOfferDocumentFileId(Long offerDocumentFileId) {
        this.offerDocumentFileId = offerDocumentFileId;
    }

    public Long getOfferDocumentId() {
        return offerDocumentId;
    }

    public void setOfferDocumentId(Long offerDocumentId) {
        this.offerDocumentId = offerDocumentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    @Override
    protected Serializable pkVal() {
        return this.offerDocumentFileId;
    }

    @Override
    public String toString() {
        return "OfferDocumentFilePO{" +
                ", offerDocumentFileId=" + offerDocumentFileId +
                ", offerDocumentId=" + offerDocumentId +
                ", fileName=" + fileName +
                ", filePath=" + filePath +
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
