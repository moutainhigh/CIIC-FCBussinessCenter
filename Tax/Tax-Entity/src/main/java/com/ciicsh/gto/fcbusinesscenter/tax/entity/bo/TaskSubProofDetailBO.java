package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import java.math.BigDecimal;
import java.util.Date;

public class TaskSubProofDetailBO {

    private Long id;

    private String employeeNo;

    private String employeeName;

    private String idType;

    private String idNo;

    private String declareAccount;

    private String incomeSubject;

    private Date incomeStart;

    private Date incomeEnd;

    private BigDecimal incomeForTax;

    private BigDecimal withholdedAmount;

    private Date createdTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }

    public String getIncomeSubject() {
        return incomeSubject;
    }

    public void setIncomeSubject(String incomeSubject) {
        this.incomeSubject = incomeSubject;
    }

    public Date getIncomeStart() {
        return incomeStart;
    }

    public void setIncomeStart(Date incomeStart) {
        this.incomeStart = incomeStart;
    }

    public Date getIncomeEnd() {
        return incomeEnd;
    }

    public void setIncomeEnd(Date incomeEnd) {
        this.incomeEnd = incomeEnd;
    }

    public BigDecimal getIncomeForTax() {
        return incomeForTax;
    }

    public void setIncomeForTax(BigDecimal incomeForTax) {
        this.incomeForTax = incomeForTax;
    }

    public BigDecimal getWithholdedAmount() {
        return withholdedAmount;
    }

    public void setWithholdedAmount(BigDecimal withholdedAmount) {
        this.withholdedAmount = withholdedAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
