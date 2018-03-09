package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom;

/**
 * Created by NeoJiang on 2018/3/8.
 */
public class PrItemsInAccountSetDTO {

    /**
     * 薪资项编码
     */
    private String payrollItemCode;
    /**
     * 薪资项别名
     */
    private String payrollItemAlias;
    /**
     * 薪资项名称
     */
    private String payrollItemName;

    public String getPayrollItemCode() {
        return payrollItemCode;
    }

    public void setPayrollItemCode(String payrollItemCode) {
        this.payrollItemCode = payrollItemCode;
    }

    public String getPayrollItemAlias() {
        return payrollItemAlias;
    }

    public void setPayrollItemAlias(String payrollItemAlias) {
        this.payrollItemAlias = payrollItemAlias;
    }

    public String getPayrollItemName() {
        return payrollItemName;
    }

    public void setPayrollItemName(String payrollItemName) {
        this.payrollItemName = payrollItemName;
    }
}
