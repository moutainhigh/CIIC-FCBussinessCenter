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

    /**
     * 供应商明细页签(1:合并列表，0:雇员列表)
     */
    private String tabType;

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

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
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
                ", tabType='" + tabType + '\'' +
                '}';
    }
}
