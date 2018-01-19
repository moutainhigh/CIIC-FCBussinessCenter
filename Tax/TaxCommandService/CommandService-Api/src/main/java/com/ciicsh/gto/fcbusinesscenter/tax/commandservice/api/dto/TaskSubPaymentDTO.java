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
     * 修改人
     */
    private String modifiedBy;

    private String taskNo;

    private BigDecimal taxAmount;

    private Integer headcount;

    private Integer chineseNum;

    private Integer foreignerNum;

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

    @Override
    public String toString() {
        return "TaskSubPaymentDTO{" +
                "id=" + id +
                ", paymentAccount='" + paymentAccount + '\'' +
                ", managerName='" + managerName + '\'' +
                ", period='" + period + '\'' +
                ", status='" + status + '\'' +
                ", periodType='" + periodType + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", subPaymentIds=" + Arrays.toString(subPaymentIds) +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", taxAmount=" + taxAmount +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                '}';
    }
}