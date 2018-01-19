package com.ciicsh.gto.salarymanagement.entity.message;

/**
 * Created by bill on 17/12/31.
 */
public class ComputeMsg {

    private String batchCode;
    private int computeStatus;


    public int getComputeStatus() {
        return computeStatus;
    }

    public void setComputeStatus(int computeStatus) {
        this.computeStatus = computeStatus;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }


}
