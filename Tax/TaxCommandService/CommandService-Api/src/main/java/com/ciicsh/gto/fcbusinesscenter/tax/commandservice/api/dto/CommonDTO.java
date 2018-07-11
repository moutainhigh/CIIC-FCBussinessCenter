package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.math.BigDecimal;

public class CommonDTO {

    //税金
    private BigDecimal taxReal;

    //税前合计
    private BigDecimal preTaxAggregate;

    //免抵税额
    private BigDecimal deduction;

    //准予扣除的捐赠额
    private BigDecimal donation;

    //允许扣除的税费
    private BigDecimal deductTakeoff;

    //其它扣除
    private BigDecimal others;

    //商业保险
    private BigDecimal businessHealthInsurance;

    //住房公积金
    private BigDecimal deductHouseFund;

    //失业保险费
    private BigDecimal deductDlenessInsurance;

    //基本医疗保险费
    private BigDecimal deductMedicalInsurance;

    //基本养老保险费
    private BigDecimal deductRetirementInsurance;

    //免税津贴
    private BigDecimal dutyFreeAllowance;

    //企业年金个人部分
    private BigDecimal annuity;

    //免税所得
    private BigDecimal incomeDutyfree;

    //收入额
    private BigDecimal incomeTotal;

    //税率
    private BigDecimal taxRate;

    //速算扣除数
    private BigDecimal quickCalDeduct;

    //合计（税前扣除项目）
    private BigDecimal deductTotal;

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getQuickCalDeduct() {
        return quickCalDeduct;
    }

    public void setQuickCalDeduct(BigDecimal quickCalDeduct) {
        this.quickCalDeduct = quickCalDeduct;
    }

    public BigDecimal getDeductTotal() {
        return deductTotal;
    }

    public void setDeductTotal(BigDecimal deductTotal) {
        this.deductTotal = deductTotal;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public BigDecimal getTaxReal() {
        return taxReal;
    }

    public void setTaxReal(BigDecimal taxReal) {
        this.taxReal = taxReal;
    }

    public BigDecimal getPreTaxAggregate() {
        return preTaxAggregate;
    }

    public void setPreTaxAggregate(BigDecimal preTaxAggregate) {
        this.preTaxAggregate = preTaxAggregate;
    }

    public BigDecimal getDonation() {
        return donation;
    }

    public void setDonation(BigDecimal donation) {
        this.donation = donation;
    }

    public BigDecimal getDeductTakeoff() {
        return deductTakeoff;
    }

    public void setDeductTakeoff(BigDecimal deductTakeoff) {
        this.deductTakeoff = deductTakeoff;
    }

    public BigDecimal getOthers() {
        return others;
    }

    public void setOthers(BigDecimal others) {
        this.others = others;
    }

    public BigDecimal getBusinessHealthInsurance() {
        return businessHealthInsurance;
    }

    public void setBusinessHealthInsurance(BigDecimal businessHealthInsurance) {
        this.businessHealthInsurance = businessHealthInsurance;
    }

    public BigDecimal getDeductHouseFund() {
        return deductHouseFund;
    }

    public void setDeductHouseFund(BigDecimal deductHouseFund) {
        this.deductHouseFund = deductHouseFund;
    }

    public BigDecimal getDeductDlenessInsurance() {
        return deductDlenessInsurance;
    }

    public void setDeductDlenessInsurance(BigDecimal deductDlenessInsurance) {
        this.deductDlenessInsurance = deductDlenessInsurance;
    }

    public BigDecimal getDeductMedicalInsurance() {
        return deductMedicalInsurance;
    }

    public void setDeductMedicalInsurance(BigDecimal deductMedicalInsurance) {
        this.deductMedicalInsurance = deductMedicalInsurance;
    }

    public BigDecimal getDeductRetirementInsurance() {
        return deductRetirementInsurance;
    }

    public void setDeductRetirementInsurance(BigDecimal deductRetirementInsurance) {
        this.deductRetirementInsurance = deductRetirementInsurance;
    }

    public BigDecimal getDutyFreeAllowance() {
        return dutyFreeAllowance;
    }

    public void setDutyFreeAllowance(BigDecimal dutyFreeAllowance) {
        this.dutyFreeAllowance = dutyFreeAllowance;
    }

    public BigDecimal getAnnuity() {
        return annuity;
    }

    public void setAnnuity(BigDecimal annuity) {
        this.annuity = annuity;
    }

    public BigDecimal getIncomeDutyfree() {
        return incomeDutyfree;
    }

    public void setIncomeDutyfree(BigDecimal incomeDutyfree) {
        this.incomeDutyfree = incomeDutyfree;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }
}
