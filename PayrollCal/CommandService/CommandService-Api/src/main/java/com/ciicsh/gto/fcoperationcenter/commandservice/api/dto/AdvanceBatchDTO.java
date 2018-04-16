package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import java.util.List;

/**
 * Created by bill on 18/4/9.
 */
public class AdvanceBatchDTO {

    private String batchCodes;

    private boolean hasAdvance;

    private String modifiedBy;


    public String getBatchCodes() {
        return batchCodes;
    }

    public void setBatchCodes(String batchCodes) {
        this.batchCodes = batchCodes;
    }

    public boolean isHasAdvance() {
        return hasAdvance;
    }

    public void setHasAdvance(boolean hasAdvance) {
        this.hasAdvance = hasAdvance;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }



}
