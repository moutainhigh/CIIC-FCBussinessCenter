package com.ciicsh.caldispatchjob.entity;

public class TaxEntity implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    public TaxEntity() {
    }

    private double taxDueStart; //应纳税额起点
    private double taxDueEnd;   //应纳终点起点
    private double taxRate;     // 税率
    private double deductions;   // 速算扣除数
    private double taxStart;    // 税额起点
    private double taxEnd;       // 税额终点
    private double afterTaxStart; // 税后工资起点
    private double afterTaxEnd;   // 税后工资终点

    public double getTaxDueStart() {
        return taxDueStart;
    }

    public void setTaxDueStart(double taxDueStart) {
        this.taxDueStart = taxDueStart;
    }

    public double getTaxDueEnd() {
        return taxDueEnd;
    }

    public void setTaxDueEnd(double taxDueEnd) {
        this.taxDueEnd = taxDueEnd;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getTaxStart() {
        return taxStart;
    }

    public void setTaxStart(double taxStart) {
        this.taxStart = taxStart;
    }

    public double getTaxEnd() {
        return taxEnd;
    }

    public void setTaxEnd(double taxEnd) {
        this.taxEnd = taxEnd;
    }

    public double getAfterTaxStart() {
        return afterTaxStart;
    }

    public void setAfterTaxStart(double afterTaxStart) {
        this.afterTaxStart = afterTaxStart;
    }

    public double getAfterTaxEnd() {
        return afterTaxEnd;
    }

    public void setAfterTaxEnd(double afterTaxEnd) {
        this.afterTaxEnd = afterTaxEnd;
    }

    @Override
    public String toString(){
        return "应纳税额起点 :" + String.valueOf(taxDueStart)
                +" 应纳终点起点:"+ String.valueOf(taxDueEnd)
                +" 税率:"+ String.valueOf(taxRate)
                +" 速算扣除数:"+ String.valueOf(deductions)
                +" 税额起点:"+ String.valueOf(taxStart)
                +" 税额终点:"+ String.valueOf(taxEnd)
                +" 税后工资起点:"+ String.valueOf(afterTaxStart)
                +" 税后工资终点:"+ String.valueOf(afterTaxEnd)
                ;
    }

}
