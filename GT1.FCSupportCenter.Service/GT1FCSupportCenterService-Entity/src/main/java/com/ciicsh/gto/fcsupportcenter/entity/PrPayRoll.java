package com.ciicsh.gto.fcsupportcenter.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by houwanhua on 2017/9/26.
 */
public class PrPayRoll {

    private long payRollId;
    private String managementId;
    private String employeeId;
    private String salaryPeriod;
    private String payRollBatch;
    private Integer channel;
    private String dataObject;
    private String remark;
    private Boolean isActive;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-MM-dd")
    private Date datachangeCreatetime;
    private Date datachangeLasttime;
    private String createdby;
    private String modifiedby;

    public long getPayRollId() {
        return payRollId;
    }

    public void setPayRollId(long payRollId) {
        this.payRollId = payRollId;
    }

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSalaryPeriod() {
        return salaryPeriod;
    }

    public void setSalaryPeriod(String salaryPeriod) {
        this.salaryPeriod = salaryPeriod;
    }

    public String getPayRollBatch() {
        return payRollBatch;
    }

    public void setPayRollBatch(String payRollBatch) {
        this.payRollBatch = payRollBatch;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getDataObject() {
        return dataObject;
    }

    public void setDataObject(String dataObject) {
        this.dataObject = dataObject;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
