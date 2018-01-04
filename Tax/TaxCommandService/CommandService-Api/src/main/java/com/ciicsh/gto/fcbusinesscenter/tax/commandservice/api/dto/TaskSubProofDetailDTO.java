package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuantongqing on 2017/12/16
 */
public class TaskSubProofDetailDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 类型
     */
    private String detailType;

    /**
     * 任务ID
     */
    private Long taskId;

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
     * 所得项目
     */
    private String incomeSubject;

    /**
     * 所得期间起
     */
    private Date incomeStart;

    /**
     * 所得期间止
     */
    private Date incomeEnd;

    /**
     * 应纳税所得额
     */
    private BigDecimal incomeForTax;

    /**
     * 扣缴税额
     */
    private BigDecimal withholdedAmount;

    /**
     * 申报账户
     */
    private String declareAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }
}
