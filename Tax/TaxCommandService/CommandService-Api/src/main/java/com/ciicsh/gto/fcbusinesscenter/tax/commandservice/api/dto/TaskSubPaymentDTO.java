package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author yuantongqing on 2018/01/02
 */
public class TaskSubPaymentDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 缴纳账户
     */
    private String paymentAccount;

    /**
     * 管理方名称
     */
    private String managerName;

    /**
     * 个税期间
     */
    private String period;

    /**
     * 状态
     */
    private String status;

    /**
     * 页签状态类别handling,completed,retreated,failed
     */
    private String statusType;

    /**
     * 页签类型：currentPan,currentBeforePan,currentAfterPan
     */
    private String periodType;

    /**
     * 当前页数
     */
    private Integer currentNum;
    /**
     * 每页显示条目
     */
    private Integer pageSize;
    /**
     * 批量完成/批量退回缴纳子任务ID
     */
    private String[] subPaymentIds;

    /**
     * 批量退回主任务ID
     */
    private Long[] mainIds;

    /**
     * 修改人
     */
    private String modifiedBy;

    private String taskNo;

    private BigDecimal taxAmount;

    private Integer headcount;

    private Integer chineseNum;

    private Integer foreignerNum;

    /**
     * 区域类型(00:本地,01:异地)
     */
    private String areaType;

    /**
     * 滞纳金
     */
    private BigDecimal overdue;
    /**
     * 罚金
     */
    private BigDecimal fine;

    /**
     * 缴纳账户名称
     */
    private String payAccountName;
    /**
     * 主任务编号
     */
    private Long taskMainId;

    public String getPayAccountName() {
        return payAccountName;
    }

    public void setPayAccountName(String payAccountName) {
        this.payAccountName = payAccountName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String[] getSubPaymentIds() {
        return subPaymentIds;
    }

    public void setSubPaymentIds(String[] subPaymentIds) {
        this.subPaymentIds = subPaymentIds;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public Integer getChineseNum() {
        return chineseNum;
    }

    public void setChineseNum(Integer chineseNum) {
        this.chineseNum = chineseNum;
    }

    public Integer getForeignerNum() {
        return foreignerNum;
    }

    public void setForeignerNum(Integer foreignerNum) {
        this.foreignerNum = foreignerNum;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Long[] getMainIds() {
        return mainIds;
    }

    public void setMainIds(Long[] mainIds) {
        this.mainIds = mainIds;
    }

    public BigDecimal getOverdue() {
        return overdue;
    }

    public void setOverdue(BigDecimal overdue) {
        this.overdue = overdue;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }

    public Long getTaskMainId() {
        return taskMainId;
    }

    public void setTaskMainId(Long taskMainId) {
        this.taskMainId = taskMainId;
    }

    @Override
    public String toString() {
        return "TaskSubPaymentDTO{" +
                "id=" + id +
                ", paymentAccount='" + paymentAccount + '\'' +
                ", managerName='" + managerName + '\'' +
                ", period='" + period + '\'' +
                ", status='" + status + '\'' +
                ", statusType='" + statusType + '\'' +
                ", periodType='" + periodType + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", subPaymentIds=" + Arrays.toString(subPaymentIds) +
                ", mainIds=" + Arrays.toString(mainIds) +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", taxAmount=" + taxAmount +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                ", areaType='" + areaType + '\'' +
                ", overdue=" + overdue +
                ", fine=" + fine +
                ", payAccountName='" + payAccountName + '\'' +
                ", taskMainId=" + taskMainId +
                '}';
    }
}
