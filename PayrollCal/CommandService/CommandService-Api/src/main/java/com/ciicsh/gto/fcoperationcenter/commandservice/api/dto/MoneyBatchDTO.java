package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

/**
 * Created by bill on 18/4/9.
 */
public class MoneyBatchDTO {

    private String batchCodes;

    private boolean hasMoney;

    private String modifiedBy;

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



}
