package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.frombatch;

import java.time.LocalDate;

/**
 * 雇员基本信息
 */
public class EmpInfoBO {

    /**
     * 雇员编号
     */
    private String employeeNo;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 工号
     */
    private String workNumber;
    /**
     * 性别
     */
    private String gender;
    /**
     * 出生日期
     */
    private LocalDate birthday;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 入职日期
     */
    private LocalDate entryDate;
    /**
     * 离职日期
     */
    private LocalDate leaveDate;

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
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

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
