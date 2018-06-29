package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author yuantongqing on 2018/02/09
 */
public class RequestForTaskSubSupplier extends PageInfo {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 批量完成/批量退回供应商子任务ID
     */
    private String[] subSupplierIds;

    /**
     * 批量退回主任务ID
     */
    private Long[] mainIds;

    /**
     * 缴纳账户
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
     * 账户类型(00:独立户,01:大库)
     */
    private String accountType;

    /**
     * 状态
     */
    private String status;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 管理方编号(管理方切换)
     */
    private String[] managerNos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getSubSupplierIds() {
        return subSupplierIds;
    }

    public void setSubSupplierIds(String[] subSupplierIds) {
        this.subSupplierIds = subSupplierIds;
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long[] getMainIds() {
        return mainIds;
    }

    public void setMainIds(Long[] mainIds) {
        this.mainIds = mainIds;
    }

    public String[] getManagerNos() {
        return managerNos;
    }

    public void setManagerNos(String[] managerNos) {
        this.managerNos = managerNos;
    }
}
