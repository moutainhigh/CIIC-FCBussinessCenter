package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom;

/**
 * Created by bill on 18/1/19.
 */
public class BatchAuditDTO {
    private String batchCode;
    private int status;
    private String comments;
    private String result;

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



}
