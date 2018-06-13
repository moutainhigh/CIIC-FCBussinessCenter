package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 薪酬计算批次状态
 * @author wuhua
 */
public enum BatchNoStatus {

    /**
     * 已关账
     */
    close("00","已关账"),

    /**
     * 取消关账
     */
    unclose("01","取消关账"),

    /**
     * 已发放
     */
    payroll("02","已发放");

    private String  code;

    private String  desc;

    private BatchNoStatus(String code,String desc)
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
