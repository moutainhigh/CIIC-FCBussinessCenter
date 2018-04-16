package com.ciicsh.gto.salarymanagement.entity.dto;

/**
 * Created by bill on 17/12/27.
 */
public class EmpFilterDTO {

    private String batchCode;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getEmpGroupCode() {
        return empGroupCode;
    }

    public void setEmpGroupCode(String empGroupCode) {
        this.empGroupCode = empGroupCode;
    }

    private String empGroupCode;



}
