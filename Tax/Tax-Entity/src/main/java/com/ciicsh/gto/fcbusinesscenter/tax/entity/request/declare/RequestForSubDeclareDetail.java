package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;


/**
 * @author yuantongqing
 * on create 2018/2/8
 */
public class RequestForSubDeclareDetail extends PageInfo {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 申报子任务ID
     */
    private Long subDeclareId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubDeclareId() {
        return subDeclareId;
    }

    public void setSubDeclareId(Long subDeclareId) {
        this.subDeclareId = subDeclareId;
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

    @Override
    public String toString() {
        return "RequestForSubDeclareDetail{" +
                "id=" + id +
                ", subDeclareId=" + subDeclareId +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                '}';
    }
}
