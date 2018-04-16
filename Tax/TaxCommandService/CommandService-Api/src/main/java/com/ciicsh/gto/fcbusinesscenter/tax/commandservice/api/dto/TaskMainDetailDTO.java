package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;


import java.math.BigDecimal;

/**
 * @author wuhua
 */
public class TaskMainDetailDTO {

    private Long taskMainDetailId;
    private BigDecimal deductRetirementInsurance;//基本养老保险费
    private BigDecimal deductMedicalInsurance;//基本医疗保险费
    private BigDecimal deductDlenessInsurance;//失业保险费
    private BigDecimal deductHouseFund;//住房公积金
    private BigDecimal incomeTotal;//收入额

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public Long getTaskMainDetailId() {
        return taskMainDetailId;
    }

    public void setTaskMainDetailId(Long taskMainDetailId) {
        this.taskMainDetailId = taskMainDetailId;
    }

    public BigDecimal getDeductRetirementInsurance() {
        return deductRetirementInsurance;
    }

    public void setDeductRetirementInsurance(BigDecimal deductRetirementInsurance) {
        this.deductRetirementInsurance = deductRetirementInsurance;
    }

    public BigDecimal getDeductMedicalInsurance() {
        return deductMedicalInsurance;
    }

    public void setDeductMedicalInsurance(BigDecimal deductMedicalInsurance) {
        this.deductMedicalInsurance = deductMedicalInsurance;
    }

    public BigDecimal getDeductDlenessInsurance() {
        return deductDlenessInsurance;
    }

    public void setDeductDlenessInsurance(BigDecimal deductDlenessInsurance) {
        this.deductDlenessInsurance = deductDlenessInsurance;
    }

    public BigDecimal getDeductHouseFund() {
        return deductHouseFund;
    }

    public void setDeductHouseFund(BigDecimal deductHouseFund) {
        this.deductHouseFund = deductHouseFund;
    }
}
