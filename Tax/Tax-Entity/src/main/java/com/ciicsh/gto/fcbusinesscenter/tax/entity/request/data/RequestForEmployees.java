package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author wuhua
 */
public class RequestForEmployees extends PageInfo {

    private Long id;

    private String employeeNo;

    private String employeeName;

    private String idType;

    private String idNo;

    private String manageNo;

    private String manageName;

    private String declareAcount;

    private Long calculationBatchId;//计算批次主表id

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

    public String getManageNo() {
        return manageNo;
    }

    public void setManageNo(String manageNo) {
        this.manageNo = manageNo;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public String getDeclareAcount() {
        return declareAcount;
    }

    public void setDeclareAcount(String declareAcount) {
        this.declareAcount = declareAcount;
    }

    public Long getCalculationBatchId() {
        return calculationBatchId;
    }

    public void setCalculationBatchId(Long calculationBatchId) {
        this.calculationBatchId = calculationBatchId;
    }
}
