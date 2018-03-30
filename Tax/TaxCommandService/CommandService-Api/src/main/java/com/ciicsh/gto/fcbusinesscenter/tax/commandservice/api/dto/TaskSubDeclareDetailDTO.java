package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.util.Arrays;

/**
 * @author yuantongqing
 * on create 2018/2/8
 */
public class TaskSubDeclareDetailDTO {
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

    /**
     * 申报明细页签(1:合并列表，0:雇员列表)
     */
    private String tabType;

    /**
     * 当前页数
     */
    private Integer currentNum;
    /**
     * 每页显示条目
     */
    private Integer pageSize;
    /**
     * 申报子任务明细ID
     */
    private Long taskSubDeclareDetailId;

    private Long[] taskSubDeclareDetailIds;

    public Long[] getTaskSubDeclareDetailIds() {
        return taskSubDeclareDetailIds;
    }

    public void setTaskSubDeclareDetailIds(Long[] taskSubDeclareDetailIds) {
        this.taskSubDeclareDetailIds = taskSubDeclareDetailIds;
    }

    public Long getTaskSubDeclareDetailId() {
        return taskSubDeclareDetailId;
    }

    public void setTaskSubDeclareDetailId(Long taskSubDeclareDetailId) {
        this.taskSubDeclareDetailId = taskSubDeclareDetailId;
    }

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

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    @Override
    public String toString() {
        return "TaskSubDeclareDetailDTO{" +
                "id=" + id +
                ", subDeclareId=" + subDeclareId +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", tabType='" + tabType + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", taskSubDeclareDetailId=" + taskSubDeclareDetailId +
                ", taskSubDeclareDetailIds=" + Arrays.toString(taskSubDeclareDetailIds) +
                '}';
    }
}
