package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;


/**
 * @author yuantongqing
 * on create 2018/2/11
 */
public class RequestForSubSupplierDetail extends PageInfo {

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

    @Override
    public String toString() {
        return "RequestForSubSupplierDetail{" +
                "id=" + id +
                ", subSupplierId=" + subSupplierId +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                '}';
    }
}
