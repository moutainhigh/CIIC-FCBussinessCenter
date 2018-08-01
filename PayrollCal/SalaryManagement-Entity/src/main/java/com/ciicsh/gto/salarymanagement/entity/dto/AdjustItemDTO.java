package com.ciicsh.gto.salarymanagement.entity.dto;

public class AdjustItemDTO {
    private String empCode;
    private String companyId;
    private double adjustVal;

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
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
