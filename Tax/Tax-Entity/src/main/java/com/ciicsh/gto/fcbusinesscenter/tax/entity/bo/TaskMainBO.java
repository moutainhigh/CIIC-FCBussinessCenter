package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import java.time.LocalDateTime;

public class TaskMainBO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 管理方编号
     */
    private String managerNo;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 状态
     */
    private String status;
    /**
     * 状态中文
     */
    private String statusName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否可用
     */
    private Boolean isActive;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 计算批次
     */
    private String batchIds;

    /**
     * 是否有合并明细
     */
    private Boolean hasCombined;

    /**
     * 创建人displayname
     */
    private String createdByDisplayName;
    /**
     * 修改人displayname
     */
    private String modifiedByDisplayName;

    //工作流id
    private Long workflowId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
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

    public String getBatchIds() {
        return batchIds;
    }

    public void setBatchIds(String batchIds) {
        this.batchIds = batchIds;
    }

    public Boolean getHasCombined() {
        return hasCombined;
    }

    public void setHasCombined(Boolean hasCombined) {
        this.hasCombined = hasCombined;
    }

    public String getCreatedByDisplayName() {
        return createdByDisplayName;
    }

    public void setCreatedByDisplayName(String createdByDisplayName) {
        this.createdByDisplayName = createdByDisplayName;
    }

    public String getModifiedByDisplayName() {
        return modifiedByDisplayName;
    }

    public void setModifiedByDisplayName(String modifiedByDisplayName) {
        this.modifiedByDisplayName = modifiedByDisplayName;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
}
