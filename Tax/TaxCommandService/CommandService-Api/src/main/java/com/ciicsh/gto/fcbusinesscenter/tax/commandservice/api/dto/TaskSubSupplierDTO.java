package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

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
     * 批量完成/批量退回供应商子任务ID
     */
    private String[] subSupplierIds;

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
                ", subSupplierIds=" + Arrays.toString(subSupplierIds) +
                '}';
    }
}