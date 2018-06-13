package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 股权收入类型
 * @author yuantongqing
 */
public enum StockIncomeType {
    /**
     * 个人股票期权行权收入-股票期权
     */
    STOCKOPTION("01","个人股票期权行权收入-股票期权"),
    /**
     * 个人股票期权行权收入-股票增值权
     */
    STOCKAPPRECIATION("02","个人股票期权行权收入-股票增值权"),
    /**
     * 个人股票期权行权收入-限制性股票
     */
    RESTRICTIVESTOCK("03","个人股票期权行权收入-限制性股票");

    private String  code;

    private String  desc;

    private StockIncomeType(String code ,String desc)
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
