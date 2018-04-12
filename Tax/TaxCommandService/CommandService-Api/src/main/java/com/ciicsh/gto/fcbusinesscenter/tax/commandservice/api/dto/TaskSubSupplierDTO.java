package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author yuantongqing on 2018/02/09
 */
public class TaskSubSupplierDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 申报账户
     */
    private String declareAccount;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 供应商名称
     */
    private String supportName;
    /**
     * 页签状态类别handling,completed,retreated,failed
     */
    private String statusType;
    /**
     * 个税期间
     */
    private String period;
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
     * 批量完成供应商合并子任务ID
     */
    private String[] subHasCombinedSupplierIds;
    /**
     * 批量完成/批量退回供应商子任务ID
     */
    private String[] subSupplierIds;

    /**
     * 批量退回主任务ID
     */
    private Long[] mainIds;

    /**
     * 主任务ID（为空，则为合并任务，被合并的子任务可能来自不同的主任务）
     */
    private Long taskMainId;
    /**
     * 供应商子任务ID（如果非空，则任务已合并，记录合并后的子任务ID）
     */
    private Long taskSubSupplierId;
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 缴纳账户
     */
    private String paymentAccount;
    /**
     * 滞纳金
     */
    private BigDecimal overdue;
    /**
     * 罚金
     */
    private BigDecimal fine;
    /**
     * 个税总金额
     */
    private BigDecimal taxAmount;
    /**
     * 总人数
     */
    private Integer headcount;
    /**
     * 中方人数
     */
    private Integer chineseNum;
    /**
     * 外方人数
     */
    private Integer foreignerNum;
    /**
     * 状态
     */
    private String status;
    /**
     * 状态中文
     */
    private String statusName;
    /**
     * 是否可用
     */
    private Boolean isActive;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 修改人
     */
    private String modifiedBy;
    /**
     * 供应商编号
     */
    private String supportNo;
    /**
     * 管理方编号
     */
    private String managerNo;
    /**
     * 是否为合并任务
     */
    private Boolean isCombined;

    /**
     * 账户类型(00:独立户,01:大库)
     */
    private String accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
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

    public String[] getSubSupplierIds() {
        return subSupplierIds;
    }

    public void setSubSupplierIds(String[] subSupplierIds) {
        this.subSupplierIds = subSupplierIds;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getTaskMainId() {
        return taskMainId;
    }

    public void setTaskMainId(Long taskMainId) {
        this.taskMainId = taskMainId;
    }

    public Long getTaskSubSupplierId() {
        return taskSubSupplierId;
    }

    public void setTaskSubSupplierId(Long taskSubSupplierId) {
        this.taskSubSupplierId = taskSubSupplierId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
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

    public String getSupportNo() {
        return supportNo;
    }

    public void setSupportNo(String supportNo) {
        this.supportNo = supportNo;
    }

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public Boolean getCombined() {
        return isCombined;
    }

    public void setCombined(Boolean combined) {
        isCombined = combined;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String[] getSubHasCombinedSupplierIds() {
        return subHasCombinedSupplierIds;
    }

    public void setSubHasCombinedSupplierIds(String[] subHasCombinedSupplierIds) {
        this.subHasCombinedSupplierIds = subHasCombinedSupplierIds;
    }

    public Long[] getMainIds() {
        return mainIds;
    }

    public void setMainIds(Long[] mainIds) {
        this.mainIds = mainIds;
    }

    @Override
    public String toString() {
        return "TaskSubSupplierDTO{" +
                "id=" + id +
                ", declareAccount='" + declareAccount + '\'' +
                ", managerName='" + managerName + '\'' +
                ", supportName='" + supportName + '\'' +
                ", statusType='" + statusType + '\'' +
                ", period='" + period + '\'' +
                ", periodType='" + periodType + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", subHasCombinedSupplierIds=" + Arrays.toString(subHasCombinedSupplierIds) +
                ", subSupplierIds=" + Arrays.toString(subSupplierIds) +
                ", mainIds=" + Arrays.toString(mainIds) +
                ", taskMainId=" + taskMainId +
                ", taskSubSupplierId=" + taskSubSupplierId +
                ", taskNo='" + taskNo + '\'' +
                ", paymentAccount='" + paymentAccount + '\'' +
                ", overdue=" + overdue +
                ", fine=" + fine +
                ", taxAmount=" + taxAmount +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                ", status='" + status + '\'' +
                ", statusName='" + statusName + '\'' +
                ", isActive=" + isActive +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", supportNo='" + supportNo + '\'' +
                ", managerNo='" + managerNo + '\'' +
                ", isCombined=" + isCombined +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
