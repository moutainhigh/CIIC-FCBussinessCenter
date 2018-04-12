package com.ciicsh.gto.salarymanagement.entity.message;

/**
 * Created by bill on 17/12/31.
 */
public class ComputeMsg {

    private String batchCode;
    private int computeStatus;
    private int batchType;

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

    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }

    public String toString(){
        return String.format("批次号：%s --批次类型：%d --计算状态：%d",batchCode,batchType,computeStatus);
    }
}
