package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

/**
 * 任务单DTO
 * @author wuhua
 */
public class OrderDTO {

    public String batchIds;//批次号

    public String taskType;//任务类型

    public String taskId;//任务id

    public String taskName;//任务名称

    public String status;//任务状态

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchIds() {
        return batchIds;
    }

    public void setBatchIds(String batchIds) {
        this.batchIds = batchIds;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
