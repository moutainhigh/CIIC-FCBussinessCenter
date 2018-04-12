package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import java.util.Date;

/**
 * Created by NeoJiang on 2018/2/2.
 *
 * @author NeoJiang
 */
public class ApprovalHistoryDTO {

    private Integer id;
    /**
     * 业务编码
     */
    private String bizCode;
    /**
     * 业务类型，枚举：
     1 表示薪资组模版；
     2 表示薪资组；
     3 表示正常批次；
     4 表示调整批次；
     5 表示回溯批次；
     */
    private Integer bizType;
    /**
     * 业务类型Label
     */
    private String bizTypeLabel;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 1 表示 审核通过
     2 表示 审核拒绝
     3 表示 待审核
     */
    private Integer approvalResult;

    private String resultLabel;
    /**
     * 审核备注
     */
    private String comments;
    /**
     * 创建人姓名（审核人姓名）
     */
    private String createdName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(Integer approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getBizTypeLabel() {
        return bizTypeLabel;
    }

    public void setBizTypeLabel(String bizTypeLabel) {
        this.bizTypeLabel = bizTypeLabel;
    }

    public String getResultLabel() {
        return resultLabel;
    }

    public void setResultLabel(String resultLabel) {
        this.resultLabel = resultLabel;
    }


    @Override
    public String toString() {
        return "ApprovalHistoryDTO{" +
                "id=" + id +
                ", bizCode='" + bizCode + '\'' +
                ", bizType=" + bizType +
                ", bizTypeLabel='" + bizTypeLabel + '\'' +
                ", createdTime=" + createdTime +
                ", createdBy='" + createdBy + '\'' +
                ", approvalResult=" + approvalResult +
                ", comments='" + comments + '\'' +
                ", createdName='" + createdName + '\'' +
                '}';
    }
}
