package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.util.Arrays;

/**
 * @author yuantongqing
 * data 2017/12/11
 */
public class TaskProofDTO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 管理方编号
     */
    private String managerNo;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 申报账户
     */
    private String declareAccount;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 起始时间
     */
    private String submitTimeStart;

    /**
     * 结束时间
     */
    private String submitTimeEnd;

    /**
     * 接受页面传的当前页面
     */
    private Integer currentNum;

    /**
     * 每页显示的条目
     */
    private Integer pageSize;

    /**
     * 提交/失效主任务ID
     */
    private String[] mainProofIds;

    /**
     * 提交/失效子任务ID
     */
    private String[] subProofIds;

    /**
     * 状态
     */
    private String status;


    /**
     * 详情页面的类型，用于判断是主任务模块进入还是子任务模块进入
     */
    private String detailType;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 雇员编号
     */
    private String employeeNo;

    /**
     * 雇员姓名
     */
    private String employeeName;

    /**
     * 所得项目
     */
    private String incomeSubject;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号
     */
    private String idNo;

    /**
     * 个税期间
     */
    private String period;
    /**
     * 任务类型(01:自动,02:人工)
     */
    private String taskType;

    /**
     * 申报账户(中文)
     */
    private String declareAccountName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
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

    public String[] getMainProofIds() {
        return mainProofIds;
    }

    public void setMainProofIds(String[] mainProofIds) {
        this.mainProofIds = mainProofIds;
    }

    public String[] getSubProofIds() {
        return subProofIds;
    }

    public void setSubProofIds(String[] subProofIds) {
        this.subProofIds = subProofIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmitTimeStart() {
        return submitTimeStart;
    }

    public void setSubmitTimeStart(String submitTimeStart) {
        this.submitTimeStart = submitTimeStart;
    }

    public String getSubmitTimeEnd() {
        return submitTimeEnd;
    }

    public void setSubmitTimeEnd(String submitTimeEnd) {
        this.submitTimeEnd = submitTimeEnd;
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

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public String getIncomeSubject() {
        return incomeSubject;
    }

    public void setIncomeSubject(String incomeSubject) {
        this.incomeSubject = incomeSubject;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDeclareAccountName() {
        return declareAccountName;
    }

    public void setDeclareAccountName(String declareAccountName) {
        this.declareAccountName = declareAccountName;
    }

    @Override
    public String toString() {
        return "TaskProofDTO{" +
                "id=" + id +
                ", managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", declareAccount='" + declareAccount + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", submitTimeStart='" + submitTimeStart + '\'' +
                ", submitTimeEnd='" + submitTimeEnd + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", mainProofIds=" + Arrays.toString(mainProofIds) +
                ", subProofIds=" + Arrays.toString(subProofIds) +
                ", status='" + status + '\'' +
                ", detailType='" + detailType + '\'' +
                ", taskId=" + taskId +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", incomeSubject='" + incomeSubject + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", period='" + period + '\'' +
                ", taskType='" + taskType + '\'' +
                ", declareAccountName='" + declareAccountName + '\'' +
                '}';
    }
}
