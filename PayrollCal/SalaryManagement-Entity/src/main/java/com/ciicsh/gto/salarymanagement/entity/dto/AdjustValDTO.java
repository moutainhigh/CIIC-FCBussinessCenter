package com.ciicsh.gto.salarymanagement.entity.dto;

public class AdjustValDTO {

    private String empCode;  //雇员编号

    private String empName; //雇员名称

    private String companyId; //公司ID

    private double adjustVal; //调整值

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public double getAdjustVal() {
        return adjustVal;
    }

    public void setAdjustVal(double adjustVal) {
        this.adjustVal = adjustVal;
    }
}
