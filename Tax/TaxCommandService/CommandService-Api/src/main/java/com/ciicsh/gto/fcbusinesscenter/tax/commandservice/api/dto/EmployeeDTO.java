package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

/**
 * @author yuantongqing on 2017/12/15
 */
public class EmployeeDTO {
    /**
     * 主键ID
     */
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
     * 管理方编号
     */
    private String manageNo;

    /**
     * 管理房名称
     */
    private String manageName;

    /**
     * 申报账户
     */
    private String declareAcount;

    private Long batchid;//计算批次主表id

    public Long getBatchid() {
        return batchid;
    }

    public void setBatchid(Long batchid) {
        this.batchid = batchid;
    }

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
