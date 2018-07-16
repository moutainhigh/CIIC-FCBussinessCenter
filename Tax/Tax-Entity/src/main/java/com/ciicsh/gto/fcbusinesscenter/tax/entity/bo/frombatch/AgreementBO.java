package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.frombatch;

import java.util.Date;

public class AgreementBO {
    /**
     * 个税期间
     */
    private Date period;
    /**
     * 申报账户
     */
    private String declareAccount;
    /**
     * 申报账户名称
     */
    private String declareAccountName;
    /**
     * 缴纳账户
     */
    private String payAccount;
    /**
     * 缴纳账户名称
     */
    private String payAccountName;
    /**
     * 供应商收款账户
     */
    private String receiptAccount;
    /**
     * 供应商编号
     */
    private String supportNo;
    /**
     * 供应商名称
     */
    private String supportName;
    /**
     * 是否供应商处理
     */
    private Boolean isSupport;
    /**
     * 是否供应商完成处理
     */
    private Boolean isSupported;
    /**
     * 是否有缴纳服务
     */
    private Boolean isPay;
    /**
     * 是否完成缴纳
     */
    private Boolean isPayed;
    /**
     * 是否有申报服务
     */
    private Boolean isDeclare;
    /**
     * 是否完成申报
     */
    private Boolean isDeclared;
    /**
     * 是否有划款服务
     */
    private Boolean isTransfer;
    /**
     * 是否完成划款
     */
    private Boolean isTransferred;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * 是否供应商申报
     */
    private Boolean isDeclareSupported;
    /**
     * 是否供应商划款
     */
    private Boolean isTransferSupported;
    /**
     * 是否供应商缴纳
     */
    private Boolean isPaySupported;
    /**
     * 是否供应商完税凭证
     */
    private Boolean isProofSupported;
    /**
     * 是否有完税凭证服务
     */
    private Boolean isProof;
    /**
     * 是否完成完税凭证
     */
    private Boolean isProofed;
    /**
     * 雇员编号
     */
    private String employeeNo;
    /**
     * 账户类型(00:独立户,01:大库)
     */
    private String accountType;
    /**
     * 区域类型(00:本地,01:异地)
     */
    private String areaType;

    private String serviceCategory;

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getDeclareAccountName() {
        return declareAccountName;
    }

    public void setDeclareAccountName(String declareAccountName) {
        this.declareAccountName = declareAccountName;
    }

    public String getPayAccountName() {
        return payAccountName;
    }

    public void setPayAccountName(String payAccountName) {
        this.payAccountName = payAccountName;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public Boolean getProofSupported() {
        return isProofSupported;
    }

    public void setProofSupported(Boolean proofSupported) {
        isProofSupported = proofSupported;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getReceiptAccount() {
        return receiptAccount;
    }

    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount;
    }

    public String getSupportNo() {
        return supportNo;
    }

    public void setSupportNo(String supportNo) {
        this.supportNo = supportNo;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }

    public Boolean getSupport() {
        return isSupport;
    }

    public void setSupport(Boolean support) {
        isSupport = support;
    }

    public Boolean getSupported() {
        return isSupported;
    }

    public void setSupported(Boolean supported) {
        isSupported = supported;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }

    public Boolean getDeclare() {
        return isDeclare;
    }

    public void setDeclare(Boolean declare) {
        isDeclare = declare;
    }

    public Boolean getDeclared() {
        return isDeclared;
    }

    public void setDeclared(Boolean declared) {
        isDeclared = declared;
    }

    public Boolean getTransfer() {
        return isTransfer;
    }

    public void setTransfer(Boolean transfer) {
        isTransfer = transfer;
    }

    public Boolean getTransferred() {
        return isTransferred;
    }

    public void setTransferred(Boolean transferred) {
        isTransferred = transferred;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Boolean getDeclareSupported() {
        return isDeclareSupported;
    }

    public void setDeclareSupported(Boolean declareSupported) {
        isDeclareSupported = declareSupported;
    }

    public Boolean getTransferSupported() {
        return isTransferSupported;
    }

    public void setTransferSupported(Boolean transferSupported) {
        isTransferSupported = transferSupported;
    }

    public Boolean getPaySupported() {
        return isPaySupported;
    }

    public void setPaySupported(Boolean paySupported) {
        isPaySupported = paySupported;
    }

    public Boolean getProof() {
        return isProof;
    }

    public void setProof(Boolean proof) {
        isProof = proof;
    }

    public Boolean getProofed() {
        return isProofed;
    }

    public void setProofed(Boolean proofed) {
        isProofed = proofed;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }
}
