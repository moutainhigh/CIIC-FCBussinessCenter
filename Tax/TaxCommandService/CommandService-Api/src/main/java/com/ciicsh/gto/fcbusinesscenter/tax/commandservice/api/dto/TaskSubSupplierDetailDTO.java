package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.math.BigDecimal;

/**
 * @author yuantongqing
 * on create 2018/2/11
 */
public class TaskSubSupplierDetailDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 供应商子任务ID
     */
    private Long subSupplierId;

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
     * 供应商明细页签(1:合并列表，0:雇员列表)
     */
    private String tabType;

    /**
     * 当前页数
     */
    private Integer currentNum;
    /**
     * 每页显示条目
     */
    private Integer pageSize;

    /**
     * 申报子任务明细ID
     */
    private Long taskSubSupplierDetailId;

    private Long[] taskSubSupplierDetailIds;

    private BigDecimal deductRetirementInsurance;//基本养老保险费
    private BigDecimal deductMedicalInsurance;//基本医疗保险费
    private BigDecimal deductDlenessInsurance;//失业保险费
    private BigDecimal deductHouseFund;//住房公积金
    private BigDecimal incomeTotal;//收入额

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

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public Long getTaskSubSupplierDetailId() {
        return taskSubSupplierDetailId;
    }

    public void setTaskSubSupplierDetailId(Long taskSubSupplierDetailId) {
        this.taskSubSupplierDetailId = taskSubSupplierDetailId;
    }

    public Long[] getTaskSubSupplierDetailIds() {
        return taskSubSupplierDetailIds;
    }

    public void setTaskSubSupplierDetailIds(Long[] taskSubSupplierDetailIds) {
        this.taskSubSupplierDetailIds = taskSubSupplierDetailIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubSupplierId() {
        return subSupplierId;
    }

    public void setSubSupplierId(Long subSupplierId) {
        this.subSupplierId = subSupplierId;
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

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    @Override
    public String toString() {
        return "TaskSubSupplierDetailDTO{" +
                "id=" + id +
                ", subSupplierId=" + subSupplierId +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", tabType='" + tabType + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
