package com.ciicsh.gto.salarymanagement.entity.dto;

import java.util.List;

/**
 * Created by bill on 18/4/22.
 */
public class ComparedAdjustBatchDTO {
    private String originCode; //原批次号
    private String batchCode; //批次号
    private String empCode;  //雇员编号
    private String empName;  //雇员名称
    private List<AdjustItem> adjustItems; //调整差异值

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public List<AdjustItem> getAdjustItems() {
        return adjustItems;
    }

    public void setAdjustItems(List<AdjustItem> adjustItems) {
        this.adjustItems = adjustItems;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }
}
