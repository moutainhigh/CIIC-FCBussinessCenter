package com.ciicsh.gto.fcbusinesscenter.site.service.entity.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 薪资发放暂缓名单导入表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
public class SalaryGrantReprieveEmployeeImportBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 薪资发放暂缓名单编号
     */
    private Long salarygrantReprieveEmployeeImportId;
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

    public Long getSalarygrantReprieveEmployeeImportId() {
        return salarygrantReprieveEmployeeImportId;
    }

    public void setSalarygrantReprieveEmployeeImportId(Long salarygrantReprieveEmployeeImportId) {
        this.salarygrantReprieveEmployeeImportId = salarygrantReprieveEmployeeImportId;
    }

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
}
