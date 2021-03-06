package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author yuantongqing on 2018/01/02
 */
public class RequestForSubMoney extends PageInfo {
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
     * 批量完成/批量退回划款子任务ID
     */
    private String[] subMoneyIds;

    /**
     * 批量退回主任务ID
     */
    private Long[] mainIds;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 区域类型(00:本地,01:异地)
     */
    private String areaType;

    /**
     * 管理方编号(管理方切换)
     */
    private String[] managerNos;

    /**
     * 缴纳账户名称
     */
    private String payAccountName;

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

    public String[] getSubMoneyIds() {
        return subMoneyIds;
    }

    public void setSubMoneyIds(String[] subMoneyIds) {
        this.subMoneyIds = subMoneyIds;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public String[] getManagerNos() {
        return managerNos;
    }

    public void setManagerNos(String[] managerNos) {
        this.managerNos = managerNos;
    }

    public String getPayAccountName() {
        return payAccountName;
    }

    public void setPayAccountName(String payAccountName) {
        this.payAccountName = payAccountName;
    }
}
