package com.ciicsh.gto.fcbusinesscenter.entity;

/**
 * Created by bill on 18/5/16.
 */
//薪资计算取消关帐消息
public class CancelClosingMsg {

    private String batchCode;
    private int batchType;
    private String optID;
    private String optName;

    private long version;

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

    public String getOptID() {
        return optID;
    }

    public void setOptID(String optID) {
        this.optID = optID;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
