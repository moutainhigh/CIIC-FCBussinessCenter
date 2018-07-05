package com.ciicsh.gto.salarymanagement.entity.po;

public class AdvanceBatchInfoPO {

    /**
     * 批次编号
     */
    private String code;

    /**
     * 逾期垫付日期
     */
    private String advancePeriod;

    /*周期垫付日*/
    private int advanceDay;

    /**
     * 是否垫付：0表示未垫付，1 表示周期垫付，2表示偶然垫付，3表示水单垫付
     */
    private int hasAdvance;

    /**
     * 批次类型
     */
    private int batchType;

    /**
     * 修改人
     */
    private String modifyBy;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdvancePeriod() {
        return advancePeriod;
    }

    public void setAdvancePeriod(String advancePeriod) {
        this.advancePeriod = advancePeriod;
    }

    public int getAdvanceDay() {
        return advanceDay;
    }

    public void setAdvanceDay(int advanceDay) {
        this.advanceDay = advanceDay;
    }

    public int getHasAdvance() {
        return hasAdvance;
    }

    public void setHasAdvance(int hasAdvance) {
        this.hasAdvance = hasAdvance;
    }

    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
}
