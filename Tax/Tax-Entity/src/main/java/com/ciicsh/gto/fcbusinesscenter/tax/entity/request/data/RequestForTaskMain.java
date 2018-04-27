package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author wuhua
 */
public class RequestForTaskMain extends PageInfo {

    private Long taskMainId;

    private String[] batchIds;

    private String[] batchNos;

    private String[] taskMainIds;

    private String tabsName;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 管理方编号
     */
    private String managerNo;
    /**
     * 计算批次号
     */
    private String batchNo;

    private String taskNo;

    private boolean isCombined;

    private Long taskMainDetailId;

    private String employeeNo;

    private String employeeName;

    private String idType;

    private String idNo;

    private String[] status;
    /**
     * 管理方名称(管理方切换)
     */
    private String[] managerNames;

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

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
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

    public String[] getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(String[] batchNos) {
        this.batchNos = batchNos;
    }

    public String[] getBatchIds() {
        return batchIds;
    }

    public void setBatchIds(String[] batchIds) {
        this.batchIds = batchIds;
    }

    public String[] getManagerNames() {
        return managerNames;
    }

    public void setManagerNames(String[] managerNames) {
        this.managerNames = managerNames;
    }
}
