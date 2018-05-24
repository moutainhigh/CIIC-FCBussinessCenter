package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import java.util.Date;

/**
 * <p>
 * 薪资发放暂缓名单导入表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
public class SalaryGrantReprieveEmployeeImportBO {
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 任务单类型:0-主表、1-中智大库、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
     */
    private Integer taskType;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 导入时间
     */
    private Date importTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 最后修改时间
     */
    private Date createdTime;
    /**
     * 最后修改人
     */
    private String modifiedBy;
    /**
     * 最后修改时间
     */
    private Date modifiedTime;

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
