package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import java.util.Objects;

/**
 * <p>
 * 雇员分组条件
 * </p>
 *
 * @author chenpb
 * @since 2018-08-07
 */
public class EmployeeOfferFileGroupExt {
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 银行卡种类 =>1：建行，2：工行，3：招商银行，4：中国银行，5：其他银行
     */
    private Integer bankcardType;
    /**
     * 付款账号
     */
    private String paymentAccountCode;
    /**
     * 付款账户名
     */
    private String paymentAccountName;
    /**
     * 付款银行名
     */
    private String paymentAccountBankName;
    /**
     * 发放币种：CNY-人民币，USD-美元，EUR-欧元
     */
    private String currencyCode;


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getBankcardType() {
        return bankcardType;
    }

    public void setBankcardType(Integer bankcardType) {
        this.bankcardType = bankcardType;
    }

    public String getPaymentAccountCode() {
        return paymentAccountCode;
    }

    public void setPaymentAccountCode(String paymentAccountCode) {
        this.paymentAccountCode = paymentAccountCode;
    }

    public String getPaymentAccountName() {
        return paymentAccountName;
    }

    public void setPaymentAccountName(String paymentAccountName) {
        this.paymentAccountName = paymentAccountName;
    }

    public String getPaymentAccountBankName() {
        return paymentAccountBankName;
    }

    public void setPaymentAccountBankName(String paymentAccountBankName) {
        this.paymentAccountBankName = paymentAccountBankName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeOfferFileGroupExt that = (EmployeeOfferFileGroupExt) o;
        return Objects.equals(companyId, that.companyId) && Objects.equals(companyName, that.companyName) && Objects.equals(bankcardType, that.bankcardType) && Objects.equals(paymentAccountCode, that.paymentAccountCode) && Objects.equals(paymentAccountName, that.paymentAccountName) && Objects.equals(paymentAccountBankName, that.paymentAccountBankName) && Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, companyName, bankcardType, paymentAccountCode, paymentAccountName, paymentAccountBankName, currencyCode);
    }
}
