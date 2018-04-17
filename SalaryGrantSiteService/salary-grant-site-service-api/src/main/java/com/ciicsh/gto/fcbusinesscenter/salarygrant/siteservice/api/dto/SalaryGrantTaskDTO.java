package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
public class SalaryGrantTaskDTO extends CommonListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务单ID
     */
    private Long taskId;
    /**
     * 任务单编号
     */
    private String taskCode;
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
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
     */
    private Integer grantType;
    /**
     * 发放类型名称
     */
    private String grantTypeName;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
    private String grantMode;
    /**
     * 发放方式名称
     */
    private String grantModeName;
    /**
     * 发放账户编号
     */
    private String grantAccountCode;
    /**
     * 发放账户名称
     */
    private String grantAccountName;
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
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待支付，6-未支付，7-已支付，8-撤回，9-驳回
     */
    private String taskStatus;
    /**
     * 状态中文描述
     */
    private String taskStatusName;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
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

    public String getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(String grantDate) {
        this.grantDate = grantDate;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public String getGrantTypeName() {
        return grantTypeName;
    }

    public void setGrantTypeName(String grantTypeName) {
        this.grantTypeName = grantTypeName;
    }

    public String getGrantMode() {
        return grantMode;
    }

    public void setGrantMode(String grantMode) {
        this.grantMode = grantMode;
    }

    public String getGrantModeName() {
        return grantModeName;
    }

    public void setGrantModeName(String grantModeName) {
        this.grantModeName = grantModeName;
    }

    public String getGrantAccountCode() {
        return grantAccountCode;
    }

    public void setGrantAccountCode(String grantAccountCode) {
        this.grantAccountCode = grantAccountCode;
    }

    public String getGrantAccountName() {
        return grantAccountName;
    }

    public void setGrantAccountName(String grantAccountName) {
        this.grantAccountName = grantAccountName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public String getApprovedOpinion() {
        return approvedOpinion;
    }

    public void setApprovedOpinion(String approvedOpinion) {
        this.approvedOpinion = approvedOpinion;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }
}
