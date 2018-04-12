package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author yuantongqing
 */
public class TaskSubMoneyBO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 主任务ID（为空，则为合并任务，被合并的子任务可能来自不同的主任务）
     */
    private Long taskMainId;
    /**
     * 划款子任务ID（如果非空，则任务已合并，记录合并后的子任务ID）
     */
    private Long taskSubMoneyId;
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 缴纳账户
     */
    private String paymentAccount;
    /**
     * 个税期间
     */
    private LocalDate period;
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
     * 管理方编号
     */
    private String managerNo;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 划款状态
     */
    private String payStatus;
    /**
     * 划款状态(中文)
     */
    private String payStatusName;

    /**
     * 付款申请ID
     */
    private Long payApplyId;

    /**
     * 付款凭证编号(结算单编号)
     */
    private String payApplyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskMainId() {
        return taskMainId;
    }

    public void setTaskMainId(Long taskMainId) {
        this.taskMainId = taskMainId;
    }

    public Long getTaskSubMoneyId() {
        return taskSubMoneyId;
    }

    public void setTaskSubMoneyId(Long taskSubMoneyId) {
        this.taskSubMoneyId = taskSubMoneyId;
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

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatusName() {
        return payStatusName;
    }

    public void setPayStatusName(String payStatusName) {
        this.payStatusName = payStatusName;
    }

    public Long getPayApplyId() {
        return payApplyId;
    }

    public void setPayApplyId(Long payApplyId) {
        this.payApplyId = payApplyId;
    }

    public String getPayApplyCode() {
        return payApplyCode;
    }

    public void setPayApplyCode(String payApplyCode) {
        this.payApplyCode = payApplyCode;
    }

    @Override
    public String toString() {
        return "TaskSubMoneyBO{" +
                "id=" + id +
                ", taskMainId=" + taskMainId +
                ", taskSubMoneyId=" + taskSubMoneyId +
                ", taskNo='" + taskNo + '\'' +
                ", paymentAccount='" + paymentAccount + '\'' +
                ", period=" + period +
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
                ", managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", payStatus='" + payStatus + '\'' +
                ", payStatusName='" + payStatusName + '\'' +
                ", payApplyId=" + payApplyId +
                ", payApplyCode='" + payApplyCode + '\'' +
                '}';
    }
}
