package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author yuantongqing
 * on create 2018/1/3
 */
public class RequestForSubPaymentDetail extends PageInfo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 缴纳子任务ID
     */
    private Long taskSubPaymentId;

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

    public Long getTaskSubPaymentId() {
        return taskSubPaymentId;
    }

    public void setTaskSubPaymentId(Long taskSubPaymentId) {
        this.taskSubPaymentId = taskSubPaymentId;
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
}