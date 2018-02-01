package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author yuantongqing
 */
public class TaskSubProofDetailBO {

    private Long id;

    /**
     * 雇员编号
     */
    private String employeeNo;

    /**
     * 雇员名称
     */
    private String employeeName;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号
     */
    private String idNo;

    /**
     * 申报账户
     */
    private String declareAccount;

    /**
     * 所得项目
     */
    private String incomeSubject;

    /**
     * 所得期间起
     */
    private LocalDate incomeStart;

    /**
     * 所得期间止
     */
    private LocalDate incomeEnd;

    /**
     * 应纳税所得额
     */
    private BigDecimal incomeForTax;

    /**
     * 扣缴税额
     */
    private BigDecimal withholdedAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


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

    public LocalDate getIncomeStart() {
        return incomeStart;
    }

    public void setIncomeStart(LocalDate incomeStart) {
        this.incomeStart = incomeStart;
    }

    public LocalDate getIncomeEnd() {
        return incomeEnd;
    }

    public void setIncomeEnd(LocalDate incomeEnd) {
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "TaskSubProofDetailBO{" +
                "id=" + id +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", declareAccount='" + declareAccount + '\'' +
                ", incomeSubject='" + incomeSubject + '\'' +
                ", incomeStart=" + incomeStart +
                ", incomeEnd=" + incomeEnd +
                ", incomeForTax=" + incomeForTax +
                ", withholdedAmount=" + withholdedAmount +
                ", createdTime=" + createdTime +
                '}';
    }
}
