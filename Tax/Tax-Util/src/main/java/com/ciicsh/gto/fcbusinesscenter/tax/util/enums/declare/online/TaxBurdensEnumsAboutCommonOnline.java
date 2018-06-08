package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.online;

/**
 * 税款负担方式
 * @author yuantongqing on 2018-06-07
 */
public enum TaxBurdensEnumsAboutCommonOnline {
    /**
     *自行负担
     */
    QUOTABURDEN("1","自行负担"),
    /**
     *雇主全额负担
     */
    PROPORTIONBURDEN("2","雇主全额负担");

    private String  code;

    private String  desc;

    private TaxBurdensEnumsAboutCommonOnline(String code ,String desc)
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
