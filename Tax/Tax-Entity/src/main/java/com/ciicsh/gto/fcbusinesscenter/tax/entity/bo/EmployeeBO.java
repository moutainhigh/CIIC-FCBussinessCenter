package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

/**
 * @author yuantongqing
 */
public class EmployeeBO {

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
     * 证件类型(中文)
     */
    private String idTypeName;

    /**
     * 证件号
     */
    private String idNo;

    /**
     * 管理方编号
     */
    private String managerNo;

    /**
     * 管理方名称
     */
    private String managerName;

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

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    @Override
    public String toString() {
        return "EmployeeBO{" +
                "id=" + id +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idTypeName='" + idTypeName + '\'' +
                ", idNo='" + idNo + '\'' +
                ", managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", declareAccount='" + declareAccount + '\'' +
                '}';
    }
}
