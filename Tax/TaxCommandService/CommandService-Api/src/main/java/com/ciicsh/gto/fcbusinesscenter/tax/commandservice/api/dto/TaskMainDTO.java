package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;


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

    @Override
    public String toString() {
        return "CalculationBatchDTO{" +
                "managerName='" + managerName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
