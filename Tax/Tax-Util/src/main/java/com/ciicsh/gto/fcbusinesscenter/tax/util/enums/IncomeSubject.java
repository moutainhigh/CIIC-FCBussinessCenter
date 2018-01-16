package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 个税所得项目
 * @author wuhua
 */
public enum IncomeSubject {

    /**
     * 工资薪金所得
     */
    IS01("工资薪金所得"),
    /**
     * 个体工商户的生产经营所得
     */
    IS02("个体工商户的生产经营所得"),
    /**
     * 企事业单位承包承租经营所得
     */
    IS03("企事业单位承包承租经营所得"),
    /**
     * 劳务报酬所得
     */
    IS04("劳务报酬所得"),
    /**
     * 稿酬所得
     */
    IS05("稿酬所得"),
    /**
     * 特许权使用费所得
     */
    IS06("特许权使用费所得"),
    /**
     * 利息股息红利所得
     */
    IS07("利息股息红利所得"),
    /**
     * 财产租赁所得
     */
    IS08("财产租赁所得"),
    /**
     * 财产转让所得
     */
    IS09("财产转让所得"),
    /**
     * 偶然所得
     */
    IS10("偶然所得"),
    /**
     * 其它所得
     */
    IS11("其它所得");

    private String  message;

    private IncomeSubject(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
