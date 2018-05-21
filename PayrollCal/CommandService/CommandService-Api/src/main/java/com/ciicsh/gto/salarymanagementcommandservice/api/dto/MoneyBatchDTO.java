package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

/**
 * Created by bill on 18/4/9.
 */
public class MoneyBatchDTO {

    private String batchCodes;

    private boolean hasMoney;

    private String modifiedBy;

    private int batchType;

    public boolean isHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(boolean hasMoney) {
        this.hasMoney = hasMoney;
    }

    public String getBatchCodes() {
        return batchCodes;
    }
    public void setBatchCodes(String batchCodes) {
        this.batchCodes = batchCodes;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }
}
