package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

/**
 * Created by bill on 18/1/19.
 */
public class BatchAuditDTO {
    private String batchCode;
    private int status;
    private String comments;
    private String result;
    private int batchType;
    private String action;
    private String modifyBy;
    private String advancePeriod;

    public String getAdvancePeriod() {
        return advancePeriod;
    }

    public void setAdvancePeriod(String advancePeriod) {
        this.advancePeriod = advancePeriod;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
