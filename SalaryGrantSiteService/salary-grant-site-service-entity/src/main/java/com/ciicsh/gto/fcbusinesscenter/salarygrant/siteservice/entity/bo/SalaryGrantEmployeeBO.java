package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放雇员信息表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-02
 */
public class SalaryGrantEmployeeBO extends SalaryGrantEmployeePO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务大类名称
     */
    private String templateTypeName;
    /**
     * 发放账户名称
     */
    private String grantAccountName;
    /**
     * 发放方式名称
     */
    private String grantModeName;
    /**
     * 发放币种名称
     */
    private String currencyName;
    /**
     * 国籍中文
     */
    private String countryName;
    /**
     * 发放金额折合人民币
     */
    private BigDecimal paymentAmountForRMB;
    /**
     * 薪资发放时段中文
     */
    private String grantTimeName;
    /**
     * 发放状态中文
     */
    private String grantStatusName;
    /**
     * 暂缓类型名称
     */
    private String reprieveTypeName;

    public String getTemplateTypeName() {
        return templateTypeName;
    }

    public void setTemplateTypeName(String templateTypeName) {
        this.templateTypeName = templateTypeName;
    }

    public String getGrantAccountName() {
        return grantAccountName;
    }

    public void setGrantAccountName(String grantAccountName) {
        this.grantAccountName = grantAccountName;
    }

    public String getGrantModeName() {
        return grantModeName;
    }

    public void setGrantModeName(String grantModeName) {
        this.grantModeName = grantModeName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public BigDecimal getPaymentAmountForRMB() {
        return paymentAmountForRMB;
    }

    public void setPaymentAmountForRMB(BigDecimal paymentAmountForRMB) {
        this.paymentAmountForRMB = paymentAmountForRMB;
    }

    public String getGrantTimeName() {
        return grantTimeName;
    }

    public void setGrantTimeName(String grantTimeName) {
        this.grantTimeName = grantTimeName;
    }

    public String getGrantStatusName() {
        return grantStatusName;
    }

    public void setGrantStatusName(String grantStatusName) {
        this.grantStatusName = grantStatusName;
    }

    public String getReprieveTypeName() {
        return reprieveTypeName;
    }

    public void setReprieveTypeName(String reprieveTypeName) {
        this.reprieveTypeName = reprieveTypeName;
    }

    @Override
    public String toString() {
        return "SalaryGrantEmployeeBO{" +
                "templateTypeName='" + templateTypeName + '\'' +
                ", grantAccountName='" + grantAccountName + '\'' +
                ", grantModeName='" + grantModeName + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", paymentAmountForRMB=" + paymentAmountForRMB +
                ", grantTimeName='" + grantTimeName + '\'' +
                ", grantStatusName='" + grantStatusName + '\'' +
                ", reprieveTypeName='" + reprieveTypeName + '\'' +
                "} " + super.toString();
    }
}
