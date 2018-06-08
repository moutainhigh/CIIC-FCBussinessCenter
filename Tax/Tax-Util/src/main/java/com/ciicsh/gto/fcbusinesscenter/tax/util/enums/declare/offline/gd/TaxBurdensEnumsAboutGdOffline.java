package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.gd;

/**
 * 税款负担方式(广东)
 * @author yuantongqing on 2018-06-05
 */
public enum TaxBurdensEnumsAboutGdOffline {

    /**
     * _01_个人负担
     */
    PERSONALBURDEN("1","_01_个人负担"),
    /**
     * _02_雇主全额负担
     */
    FULLBURDENS("2","_02_雇主全额负担"),
    /**
     * _03_雇主定额负担
     */
    QUOTABURDEN("3","_03_雇主定额负担"),
    /**
     * _04_雇主比例负担
     */
    PROPORTIONBURDEN("4","_04_雇主比例负担");

    private String  code;

    private String  desc;

    private TaxBurdensEnumsAboutGdOffline(String code ,String desc)
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
