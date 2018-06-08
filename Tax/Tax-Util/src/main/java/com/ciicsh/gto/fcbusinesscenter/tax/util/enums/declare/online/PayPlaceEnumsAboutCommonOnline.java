package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.online;

/**
 * 支付地
 * @author yuantongqing on 2018-06-07
 */
public enum PayPlaceEnumsAboutCommonOnline {

    /**
     * 境内支付
     */
    DOMESTICPAYMENT("1","境内支付"),
    /**
     * 境外支付
     */
    OVERSEASPAYMENT("2","境外支付"),
    /**
     * 境内、外同时支付
     */
    SIMULTANEOUSPAYMENT("3","境内、外同时支付");

    private String  code;

    private String  desc;

    private PayPlaceEnumsAboutCommonOnline(String code , String desc)
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
