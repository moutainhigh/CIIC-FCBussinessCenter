package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.sz;

public enum IdTypeAboutSzReductionOffline {
    /**
     * Resident identity card
     */
    RESIDENTIDENTITYCARD("1","201 居民身份证"),
    /**
     * certificate of officers
     */
    CERTIFICATEOFOFFICERS("2","202 军官证"),
    /**
     * Soldier's card
     */
    SOLDIERCARD("3","204 士兵证"),
    /**
     * Police officer card of Armed Police
     */
    ARMEDPOLICECARD("4","203 武警警官证"),
    /**
     * Hong Kong and Macao residents' passport to the mainland
     */
    HKAMRPTTM("5","210 港澳居民来往内地通行证"),
    /**
     * travel passes for Taiwan residents to enter or leave the mainland
     */
    TPFTWRTEOLTM("6","213 台湾居民来往大陆通行证"),
    /**
     * Chinese passport
     */
    CHINESEPASSPORT("7","227 中国护照"),
    /**
     * Foreign passport
     */
    FOREIGNPASSPORT("8","208 外国护照"),
    /**
     * Permanent resident identity card in Hongkong
     */
    HONGKONGIDENTITYCARD("9","219 香港身份证"),
    /**
     * Taiwan identity card
     */
    TAIWANIDENTITYCARD("10","220 台湾身份证"),
    /**
     * Permanent resident identity card of the Macao Special Administrative Region
     */
    PRIDOTMSAR("11","221 澳门身份证"),
    /**
     * Permanent residence permit for foreigners
     */
    PRPFF("12","233 外国人永久居留证");

    private String  code;

    private String  desc;

    private IdTypeAboutSzReductionOffline(String code , String desc)
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
