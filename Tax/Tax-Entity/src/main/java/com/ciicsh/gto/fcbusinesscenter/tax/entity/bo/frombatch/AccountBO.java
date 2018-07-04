package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.frombatch;

public class AccountBO {

    /**
     * 计算批次ID
     */
    private Long calBatchId;
    /**
     * 引擎计算批次号
     */
    private String batchNo;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 纳税人识别号
     */
    private String accountNumber;
    /**
     * 委托合同流水号
     */
    private String commissionContractSerialNumber;
    /**
     * 省份代码
     */
    private String provinceCode;
    /**
     * 城市代码
     */
    private String cityCode;
    /**
     * 纳税分局
     */
    private String substation;
    /**
     * 纳税人开户名
     */
    private String taxpayerAccountName;
    /**
     * 纳税人专户帐号
     */
    private String account;
    /**
     * 纳税专户开户银行
     */
    private String taxAccountOpeningBank;
    /**
     * 纳税人名称
     */
    private String taxpayerName;
    /**
     * 所属税务局
     */
    private String station;
    /**
     * 来源 0：大库 1：独立库
     */
    private String source;

    public Long getCalBatchId() {
        return calBatchId;
    }

    public void setCalBatchId(Long calBatchId) {
        this.calBatchId = calBatchId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCommissionContractSerialNumber() {
        return commissionContractSerialNumber;
    }

    public void setCommissionContractSerialNumber(String commissionContractSerialNumber) {
        this.commissionContractSerialNumber = commissionContractSerialNumber;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getSubstation() {
        return substation;
    }

    public void setSubstation(String substation) {
        this.substation = substation;
    }

    public String getTaxpayerAccountName() {
        return taxpayerAccountName;
    }

    public void setTaxpayerAccountName(String taxpayerAccountName) {
        this.taxpayerAccountName = taxpayerAccountName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTaxAccountOpeningBank() {
        return taxAccountOpeningBank;
    }

    public void setTaxAccountOpeningBank(String taxAccountOpeningBank) {
        this.taxAccountOpeningBank = taxAccountOpeningBank;
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
