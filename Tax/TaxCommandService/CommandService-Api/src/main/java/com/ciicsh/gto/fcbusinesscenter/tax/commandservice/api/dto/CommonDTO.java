package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.math.BigDecimal;

public class CommonDTO {

    //税金
    private BigDecimal tax;

    //社保公积金总额
    private BigDecimal socialTotal;

    //收入额
    private BigDecimal incomeTotal;

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getSocialTotal() {
        return socialTotal;
    }

    public void setSocialTotal(BigDecimal socialTotal) {
        this.socialTotal = socialTotal;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }
}
