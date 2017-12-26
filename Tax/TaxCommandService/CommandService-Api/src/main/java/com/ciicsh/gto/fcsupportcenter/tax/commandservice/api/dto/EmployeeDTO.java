package com.ciicsh.gto.fcsupportcenter.tax.commandservice.api.dto;

/**
 * @author yuantongqing on 2017/12/15
 */
public class EmployeeDTO {
    private Long id;

    private String employeeNo;

    private String employeeName;

    private String idType;

    private String idNo;

    private String manageNo;

    private String manageName;

    private String declareAcount;

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

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", manageNo='" + manageNo + '\'' +
                ", manageName='" + manageName + '\'' +
                ", declareAcount='" + declareAcount + '\'' +
                '}';
    }
}
