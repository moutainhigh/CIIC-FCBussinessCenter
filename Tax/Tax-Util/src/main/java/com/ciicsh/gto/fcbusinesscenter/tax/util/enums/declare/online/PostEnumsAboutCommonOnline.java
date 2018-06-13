package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.online;

/**
 * 线上模板职务
 * @author yuantongqing on 2018-06-07
 */
public enum PostEnumsAboutCommonOnline {

    /**
     * 高层
     */
    HIGHLEVEL("1", "高层"),
    /**
     * 中层
     */
    MIDDLELEVEL("2", "中层"),
    /**
     * 普通
     */
    COMMON("3", "普通");

    private String code;

    private String desc;

    private PostEnumsAboutCommonOnline(String code, String desc) {
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
