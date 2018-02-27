package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.util.Arrays;

/**
 * @author yuantongqing
 * on create 2018/2/5
 */
public class CalculationBatchDetailDTO {
    /**
     * 雇员编号
     */
    private String employeeNo;
    /**
     * 雇员姓名
     */
    private String employeeName;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 计算批次号
     */
    private String batchNo;
    /**
     * 当前页数
     */
    private Integer currentNum;
    /**
     * 每页显示条目
     */
    private Integer pageSize;

    /**
     * 批量恢复/批量失效暂缓批次ID
     */
    private String[] ids;

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

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "CalculationBatchDetailDTO{" +
                "employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", managerName='" + managerName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", ids=" + Arrays.toString(ids) +
                '}';
    }
}
