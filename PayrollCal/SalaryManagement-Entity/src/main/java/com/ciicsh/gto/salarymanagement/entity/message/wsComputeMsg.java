package com.ciicsh.gto.salarymanagement.entity.message;

/**
 * Created by bill on 18/1/11.
 */
public class wsComputeMsg {

    private String batchCode;

    private int batchType;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }
}
