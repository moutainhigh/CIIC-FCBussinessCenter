package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.frombatch;

public class SupplierBO {
    /**
     * 供应商ID
     */
    private String supplierId;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 帐号
     */
    private String account;
    /**
     * 开户银行
     */
    private String taxAccountOpeningBank;
    /**
     * 省份代码
     */
    private String provinceCode;
    /**
     * 城市代码
     */
    private String cityCode;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
}
