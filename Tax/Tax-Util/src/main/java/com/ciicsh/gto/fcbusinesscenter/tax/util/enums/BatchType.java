package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 薪酬计算批次类型
 * @author wuhua
 */
public enum BatchType {

    /**
     * 正常
     */
    normal("00","正常"),

    /**
     * 调整
     */
    ajust("01","调整"),

    /**
     * 回溯
     */
    backdate("02","回溯");

    private String  code;

    private String  desc;

    private BatchType(String code, String desc)
    {

        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
