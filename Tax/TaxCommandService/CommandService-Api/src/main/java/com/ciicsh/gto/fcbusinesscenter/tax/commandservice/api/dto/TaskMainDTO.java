package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;


import java.util.Arrays;

/**
 * @author wuhua
 */
public class TaskMainDTO {

    private String managerName;
    private String batchNo;
    private String[] taskMainIds;
    private Long taskMainId;
    private String taskNo;
    private String tabsName;
    private Integer currentNum;
    private Integer pageSize;
    private boolean isCombined;
    private Long taskMainDetailId;
    private Long[] taskMainDetailIds;
    private String employeeNo;
    private String employeeName;
    private String idType;
    private String idNo;
    private String[] status;
    /**
     * 批次号数组
     */
    private String[] batchNos;
    private String[] taskNos;

    public String[] getTaskNos() {
        return taskNos;
    }

    public void setTaskNos(String[] taskNos) {
        this.taskNos = taskNos;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Long[] getTaskMainDetailIds() {
        return taskMainDetailIds;
    }

    public void setTaskMainDetailIds(Long[] taskMainDetailIds) {
        this.taskMainDetailIds = taskMainDetailIds;
    }

    public Long getTaskMainDetailId() {
        return taskMainDetailId;
    }

    public void setTaskMainDetailId(Long taskMainDetailId) {
        this.taskMainDetailId = taskMainDetailId;
    }

    public boolean getIsCombined() {
        return isCombined;
    }

    public void setIsCombined(boolean combined) {
        isCombined = combined;
    }

    public Long getTaskMainId() {
        return taskMainId;
    }

    public void setTaskMainId(Long taskMainId) {
        this.taskMainId = taskMainId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTabsName() {
        return tabsName;
    }

    public void setTabsName(String tabsName) {
        this.tabsName = tabsName;
    }

    public String[] getTaskMainIds() {
        return taskMainIds;
    }

    public void setTaskMainIds(String[] taskMainIds) {
        this.taskMainIds = taskMainIds;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(String[] batchNos) {
        this.batchNos = batchNos;
    }

    public boolean isCombined() {
        return isCombined;
    }

    public void setCombined(boolean combined) {
        isCombined = combined;
    }

    @Override
    public String toString() {
        return "TaskMainDTO{" +
                "managerName='" + managerName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", taskMainIds=" + Arrays.toString(taskMainIds) +
                ", taskMainId=" + taskMainId +
                ", taskNo='" + taskNo + '\'' +
                ", tabsName='" + tabsName + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", isCombined=" + isCombined +
                ", taskMainDetailId=" + taskMainDetailId +
                ", taskMainDetailIds=" + Arrays.toString(taskMainDetailIds) +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", status=" + Arrays.toString(status) +
                ", batchNos=" + Arrays.toString(batchNos) +
                '}';
    }
}
