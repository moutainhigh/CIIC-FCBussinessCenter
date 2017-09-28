package com.ciicsh.gto.fcsupportcenter.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by houwanhua on 2017/9/27.
 */
public class SsEmpRefund {
    private Integer empRefundId;
    private String employeeTaskId;
    private String empArchiveId;
    private Double amount;
    private Date processTime;
    private Integer processWay;
    private Boolean isActive;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-MM-dd")
    private Date datachangeCreatetime;
    private Date datachangeLasttime;
    private String createdby;
    private String modifiedby;

    public Integer getEmpRefundId() {
        return empRefundId;
    }

    public void setEmpRefundId(Integer empRefundId) {
        this.empRefundId = empRefundId;
    }

    public String getEmployeeTaskId() {
        return employeeTaskId;
    }

    public void setEmployeeTaskId(String employeeTaskId) {
        this.employeeTaskId = employeeTaskId;
    }

    public String getEmpArchiveId() {
        return empArchiveId;
    }

    public void setEmpArchiveId(String empArchiveId) {
        this.empArchiveId = empArchiveId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Integer getProcessWay() {
        return processWay;
    }

    public void setProcessWay(Integer processWay) {
        this.processWay = processWay;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getDatachangeCreatetime() {
        return datachangeCreatetime;
    }

    public void setDatachangeCreatetime(Date datachangeCreatetime) {
        this.datachangeCreatetime = datachangeCreatetime;
    }

    public Date getDatachangeLasttime() {
        return datachangeLasttime;
    }

    public void setDatachangeLasttime(Date datachangeLasttime) {
        this.datachangeLasttime = datachangeLasttime;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }
}
