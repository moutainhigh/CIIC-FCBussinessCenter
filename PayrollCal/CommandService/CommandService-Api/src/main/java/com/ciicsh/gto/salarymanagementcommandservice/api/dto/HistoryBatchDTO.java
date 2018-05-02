package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

/**
 * Created by bill on 18/4/25.
 */
public class HistoryBatchDTO {
    private String normalBatchCode;
    private String accountSetCode;
    private String managerId;

    public String getNormalBatchCode() {
        return normalBatchCode;
    }

    public void setNormalBatchCode(String normalBatchCode) {
        this.normalBatchCode = normalBatchCode;
    }

    public String getAccountSetCode() {
        return accountSetCode;
    }

    public void setAccountSetCode(String accountSetCode) {
        this.accountSetCode = accountSetCode;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
