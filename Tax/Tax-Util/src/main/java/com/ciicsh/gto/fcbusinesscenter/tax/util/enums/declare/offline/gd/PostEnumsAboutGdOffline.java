package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.gd;

/**
 * 线下模板职务
 * @author yuantongqing on 2018-06-06
 */
public enum PostEnumsAboutGdOffline {

    /**
     * 高层
     */
    HIGHLEVEL("1", "_1_高层"),
    /**
     * 中层
     */
    MIDDLELEVEL("2", "_2_中层"),
    /**
     * 普通
     */
    COMMON("3", "_9_普通");

    private String code;

    private String desc;

    private PostEnumsAboutGdOffline(String code, String desc) {
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
