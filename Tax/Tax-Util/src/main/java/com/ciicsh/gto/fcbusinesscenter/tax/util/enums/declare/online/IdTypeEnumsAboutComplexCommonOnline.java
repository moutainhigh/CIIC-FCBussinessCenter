package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.online;

/**
 * 线上证件类型(外国人永久居留证全写)
 * @author yuantongqing on 2018-06-07
 */
public enum IdTypeEnumsAboutComplexCommonOnline {
    /**
     * Resident identity card
     */
    RESIDENTIDENTITYCARD("1","居民身份证"),
    /**
     * certificate of officers
     */
    CERTIFICATEOFOFFICERS("2","军官证"),
    /**
     * Soldier's card
     */
    SOLDIERCARD("3","士兵证"),
    /**
     * Police officer card of Armed Police
     */
    ARMEDPOLICECARD("4","武警警官证"),
    /**
     * Hong Kong and Macao residents' passport to the mainland
     */
    HKAMRPTTM("5","港澳居民来往内地通行证"),
    /**
     * travel passes for Taiwan residents to enter or leave the mainland
     */
    TPFTWRTEOLTM("6","台湾居民来往大陆通行证"),
    /**
     * Chinese passport
     */
    CHINESEPASSPORT("7","中国护照"),
    /**
     * Foreign passport
     */
    FOREIGNPASSPORT("8","外国护照"),
    /**
     * Permanent resident identity card in Hongkong
     */
    HONGKONGIDENTITYCARD("9","香港永久性居民身份证"),
    /**
     * Taiwan identity card
     */
    TAIWANIDENTITYCARD("10","台湾身份证"),
    /**
     * Permanent resident identity card of the Macao Special Administrative Region
     */
    PRIDOTMSAR("11","澳门特别行政区永久性居民身份证"),
    /**
     * Permanent residence permit for foreigners
     */
    PRPFF("12","外国人永久居留身份证（外国人永久居留证）");

    private String  code;

    private String  desc;

    private IdTypeEnumsAboutComplexCommonOnline(String code , String desc)
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
