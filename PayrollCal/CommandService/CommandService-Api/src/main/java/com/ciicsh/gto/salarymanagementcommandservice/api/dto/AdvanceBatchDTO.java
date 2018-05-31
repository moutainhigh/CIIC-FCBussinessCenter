package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import java.util.List;

/**
 * Created by bill on 18/4/9.
 */
public class AdvanceBatchDTO {

    private String batchCodes;

    private int batchType;

    private int advance;

    private String modifiedBy;

    public int getAdvance() {
        return advance;
    }

    public void setAdvance(int advance) {
        this.advance = advance;
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
