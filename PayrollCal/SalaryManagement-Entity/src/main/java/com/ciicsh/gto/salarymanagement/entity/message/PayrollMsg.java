package com.ciicsh.gto.salarymanagement.entity.message;

/**
 * Created by bill on 17/12/5.
 */
public class PayrollMsg {

    private String batchId;
    private String empGroupId;

    public String getEmpGroupId() {
        return empGroupId;
    }

    public void setEmpGroupId(String empGroupId) {
        this.empGroupId = empGroupId;
    }

    public String getPayrollGroupId() {
        return payrollGroupId;
    }

    public void setPayrollGroupId(String payrollGroupId) {
        this.payrollGroupId = payrollGroupId;
    }

    private String payrollGroupId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String toString() {
        return "PayrollMsg{" +
                "batchId=" + batchId +
                ", empGroupId=" + empGroupId +
                ", payrollGroupId=" + payrollGroupId +
                "}";
    }

}
