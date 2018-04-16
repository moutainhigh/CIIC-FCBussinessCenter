package com.ciicsh.gto.fcbusinesscenter.site.service.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单历史表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
public class SalaryGrantTaskHistoryBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务单历史编号
     */
    private Long salaryGrantTaskHistoryId;
    /**
     * 任务单ID
     */
    private Long taskId;
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 流程编号
     */
    private String workFlowProcessId;
    /**
     * 管理方编号
     */
    private String managementId;
    /**
     * 管理方名称
     */
    private String managementName;
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 参考批次号
     */
    private String originBatchCode;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 薪资发放总金额（RMB）
     */
    private BigDecimal paymentTotalSum;
    /**
     * 发薪人数
     */
    private Integer totalPersonCount;
    /**
     * 中方发薪人数
     */
    private Integer chineseCount;
    /**
     * 外方发薪人数
     */
    private Integer foreignerCount;
    /**
     * 中智上海发薪人数
     */
    private Integer localGrantCount;
    /**
     * 中智代发（委托机构）发薪人数
     */
    private Integer supplierGrantCount;
    /**
     * 薪资发放日期
     */
    private String grantDate;
    /**
     * 薪资发放时段:1-上午，2-下午
     */
    private Integer grantTime;
    /**
     * 发放账户编号
     */
    private String grantAccountCode;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
     */
    private Integer grantType;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
    private String grantMode;
    /**
     * 是否内转:0-非内转，1-内转
     */
    private Boolean isAdversion;
    /**
     * 内转类型:1-AF转FC,2-BPO转FC,3-FC转AF,4-FC转BPO
     */
    private Integer adversionType;
    /**
     * 备注:任务单中雇员信息变化提示链接
     */
    private String remark;
    /**
     * 失效原因
     */
    private String invalidReason;
    /**
     * 审批意见
     */
    private String approvedOpinion;
    /**
     * 状态:2-审批通过，3-审批拒绝，4-失效，8-驳回
     */
    private String taskStatus;
    /**
     * 任务单类型:0-主表、1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
    private Integer taskType;
    /**
     * 外币发放标识:0-否，1-是
     */
    private Boolean isIncludeForeignCurrency;
    /**
     * 操作员
     */
    private String operatorUserId;
    /**
     * 审核员
     */
    private String approveUserId;

    public Long getSalaryGrantTaskHistoryId() {
        return salaryGrantTaskHistoryId;
    }

    public void setSalaryGrantTaskHistoryId(Long salaryGrantTaskHistoryId) {
        this.salaryGrantTaskHistoryId = salaryGrantTaskHistoryId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getWorkFlowProcessId() {
        return workFlowProcessId;
    }

    public void setWorkFlowProcessId(String workFlowProcessId) {
        this.workFlowProcessId = workFlowProcessId;
    }

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public String getManagementName() {
        return managementName;
    }

    public void setManagementName(String managementName) {
        this.managementName = managementName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getGrantCycle() {
        return grantCycle;
    }

    public void setGrantCycle(String grantCycle) {
        this.grantCycle = grantCycle;
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

    public Integer getChineseCount() {
        return chineseCount;
    }

    public void setChineseCount(Integer chineseCount) {
        this.chineseCount = chineseCount;
    }

    public Integer getForeignerCount() {
        return foreignerCount;
    }

    public void setForeignerCount(Integer foreignerCount) {
        this.foreignerCount = foreignerCount;
    }

    public Integer getLocalGrantCount() {
        return localGrantCount;
    }

    public void setLocalGrantCount(Integer localGrantCount) {
        this.localGrantCount = localGrantCount;
    }

    public Integer getSupplierGrantCount() {
        return supplierGrantCount;
    }

    public void setSupplierGrantCount(Integer supplierGrantCount) {
        this.supplierGrantCount = supplierGrantCount;
    }

    public String getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(String grantDate) {
        this.grantDate = grantDate;
    }

    public Integer getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(Integer grantTime) {
        this.grantTime = grantTime;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public String getGrantMode() {
        return grantMode;
    }

    public void setGrantMode(String grantMode) {
        this.grantMode = grantMode;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getOriginBatchCode() {
        return originBatchCode;
    }

    public void setOriginBatchCode(String originBatchCode) {
        this.originBatchCode = originBatchCode;
    }

    public String getGrantAccountCode() {
        return grantAccountCode;
    }

    public void setGrantAccountCode(String grantAccountCode) {
        this.grantAccountCode = grantAccountCode;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Boolean getAdversion() {
        return isAdversion;
    }

    public void setAdversion(Boolean adversion) {
        isAdversion = adversion;
    }

    public Integer getAdversionType() {
        return adversionType;
    }

    public void setAdversionType(Integer adversionType) {
        this.adversionType = adversionType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovedOpinion() {
        return approvedOpinion;
    }

    public void setApprovedOpinion(String approvedOpinion) {
        this.approvedOpinion = approvedOpinion;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(String approveUserId) {
        this.approveUserId = approveUserId;
    }

    public Boolean getIncludeForeignCurrency() {
        return isIncludeForeignCurrency;
    }

    public void setIncludeForeignCurrency(Boolean includeForeignCurrency) {
        isIncludeForeignCurrency = includeForeignCurrency;
    }
}
