package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.online;

/**
 * 线上模板适用公式
 *
 * @author yuantongqing on 2018-06-07
 */
public enum ApplicableFormulaEnumsAboutCommonOnline {
    /**
     * 不超过90天或183天
     */
    AF01("1", "公式(1) 不超过90天或183天"),
    /**
     * 超过90天或183天但不满1年
     */
    AF02("2", "公式(2) 超过90天或183天但不满1年"),
    /**
     * 满1年不超过5年(外籍高管满90或183天不超过5年)
     */
    AF03("3", "公式(3) 满1年不超过5年(外籍高管满90或183天不超过5年)"),
    /**
     * 居住满5年以上
     */
    AF04("4", "公式(4) 居住满5年以上"),
    /**
     * 外籍高管不超过90天或183天
     */
    AF05("5", "公式(5) 外籍高管不超过90天或183天");
    private String code;

    private String desc;

    private ApplicableFormulaEnumsAboutCommonOnline(String code, String desc) {
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
