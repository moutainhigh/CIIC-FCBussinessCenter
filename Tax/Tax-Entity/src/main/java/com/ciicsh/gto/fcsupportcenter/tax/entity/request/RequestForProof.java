package com.ciicsh.gto.fcsupportcenter.tax.entity.request;


import com.ciicsh.gto.fcsupportcenter.tax.entity.page.PageInfo;

/**
 * @author yuantongqing on 2017/12/13
 */
public class RequestForProof extends PageInfo {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 起始时间
     */
    private String submitTimeStart;

    /**
     * 结束时间
     */
    private String submitTimeEnd;

    /**
     * 提交/失效主任务ID
     */
    private String[] mainProofIds;

    /**
     * 提交/失效/合并子任务ID
     */
    private String[] subProofIds;

    /**
     * 状态
     */
    private String status;

    /**
     * 修改人
     */
    private String modifiedBy;

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
     * 证件类型
     */
    private String idType;

    /**
     * 证件号
     */
    private String idNo;

    /**
     * 所得项目
     */
    private String incomeSubject;

    /**
     * 申报账户
     */
    private String declareAccount;

    /**
     * 个税期间
     */
    private String period;
    /**
     * 任务类型(01:自动,02:人工)
     */
    private String taskType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
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

    public String getIncomeSubject() {
        return incomeSubject;
    }

    public void setIncomeSubject(String incomeSubject) {
        this.incomeSubject = incomeSubject;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
