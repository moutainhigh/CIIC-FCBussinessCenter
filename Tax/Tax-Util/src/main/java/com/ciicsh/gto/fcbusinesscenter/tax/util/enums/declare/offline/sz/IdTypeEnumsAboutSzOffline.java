package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.sz;

public enum IdTypeEnumsAboutSzOffline {
    /**
     * Resident identity card
     */
    RESIDENTIDENTITYCARD("1","_201_居民身份证"),
    /**
     * certificate of officers
     */
    CERTIFICATEOFOFFICERS("2","_202_军官证"),
    /**
     * Soldier's card
     */
    SOLDIERCARD("3","_204_士兵证"),
    /**
     * Police officer card of Armed Police
     */
    ARMEDPOLICECARD("4","_203_武警警官证"),
    /**
     * Hong Kong and Macao residents' passport to the mainland
     */
    HKAMRPTTM("5","_210_港澳居民来往内地通行证"),
    /**
     * travel passes for Taiwan residents to enter or leave the mainland
     */
    TPFTWRTEOLTM("6","_213_台湾居民来往大陆通行证"),
    /**
     * Chinese passport
     */
    CHINESEPASSPORT("7","_227_中国护照"),
    /**
     * Foreign passport
     */
    FOREIGNPASSPORT("8","_208_外国护照"),
    /**
     * Permanent resident identity card in Hongkong
     */
    HONGKONGIDENTITYCARD("9","_219_香港身份证"),
    /**
     * Taiwan identity card
     */
    TAIWANIDENTITYCARD("10","_220_台湾身份证"),
    /**
     * Permanent resident identity card of the Macao Special Administrative Region
     */
    PRIDOTMSAR("11","_221_澳门身份证"),
    /**
     * Permanent residence permit for foreigners
     */
    PRPFF("12","_233_外国人永久居留证");

    private String  code;

    private String  desc;

    private IdTypeEnumsAboutSzOffline(String code , String desc)
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
