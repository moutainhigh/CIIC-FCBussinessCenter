package com.ciicsh.gto.fcbusinesscenter.site.service.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单子表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-23
 */
public class SalaryGrantSubTaskBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务单ID
     */
    private Long salaryGrantSubTaskId;
    /**
     * 任务单编号
     */
    private String salaryGrantSubTaskCode;
    /**
     * 任务单主表编号
     */
    private String salaryGrantMainTaskCode;
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
    private Integer grantMode;
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
     * 审批意见
     */
    private String approvedOpinion;
    /**
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待支付，6-未支付，7-已支付，8-撤回，9-驳回，10-待合并，11-已合并，12-已确认
     */
    private String taskStatus;
    /**
     * 任务单类型:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
    private Integer taskType;
    /**
     * 操作员
     */
    private String operatorUserId;
    /**
     * 审核员
     */
    private String approveUserId;

    public Long getSalaryGrantSubTaskId() {
        return salaryGrantSubTaskId;
    }

    public void setSalaryGrantSubTaskId(Long salaryGrantSubTaskId) {
        this.salaryGrantSubTaskId = salaryGrantSubTaskId;
    }

    public String getSalaryGrantSubTaskCode() {
        return salaryGrantSubTaskCode;
    }

    public void setSalaryGrantSubTaskCode(String salaryGrantSubTaskCode) {
        this.salaryGrantSubTaskCode = salaryGrantSubTaskCode;
    }

    public String getSalaryGrantMainTaskCode() {
        return salaryGrantMainTaskCode;
    }

    public void setSalaryGrantMainTaskCode(String salaryGrantMainTaskCode) {
        this.salaryGrantMainTaskCode = salaryGrantMainTaskCode;
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

    public String getGrantAccountCode() {
        return grantAccountCode;
    }

    public void setGrantAccountCode(String grantAccountCode) {
        this.grantAccountCode = grantAccountCode;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public Integer getGrantMode() {
        return grantMode;
    }

    public void setGrantMode(Integer grantMode) {
        this.grantMode = grantMode;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
