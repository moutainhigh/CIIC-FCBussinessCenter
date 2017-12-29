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

    public String getEmpCodes() {
        return empCodes;
    }

    public void setEmpCodes(String empCodes) {
        this.empCodes = empCodes;
    }

    private String empCodes;



}
