package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.sz;

/**
 * 深圳线下所得项目
 * @author yuantongqing on 2018-06-05
 */
public enum IncomeSubjectEnumsAboutSzOffline {
    /**
     * 0101 正常工资薪金
     */
    NORMALSALARY("01","0101 正常工资薪金"),
    /**
     * 0102 外籍人员正常工资薪金
     */
    FOREIGNNORMALSALARY("02","0102 外籍人员正常工资薪金"),
    /**
     * 0103 全年一次性奖金收入
     */
    BONUSINCOMEYEAR("03","0103 全年一次性奖金收入"),
    /**
     * 1000 偶然所得
     */
    FORTUITOUSINCOME("04","1000 偶然所得"),
    /**
     * 0700 利息、股息、红利所得
     */
    IDDINCOME("05","0700 利息、股息、红利所得"),
    /**
     * 0400 劳务报酬所得
     */
    LABORINCOME("06","0400 劳务报酬所得"),
    /**
     * 0108 解除劳动合同一次性补偿金
     */
    LABORCONTRACT("07","0108 解除劳动合同一次性补偿金"),
    /**
     * 0109 个人股票期权行权收入
     */
    STOCKOPTIONINCOME("08","0109 个人股票期权行权收入"),
    /**
     * 0104 外籍人员数月奖金
     */
    NOP1("","0104 外籍人员数月奖金"),
    /**
     * 0107 内退一次性补偿金
     */
    NOP2("","0107 内退一次性补偿金"),
    /**
     * 0500 稿酬所得
     */
    NOP3("","0500 稿酬所得"),
    /**
     * 0600 特许权使用费所得
     */
    NOP4("","0600 特许权使用费所得"),
    /**
     * 9900 其他所得
     */
    NOP5("","9900 其他所得"),
    /**
     * 0904 财产拍卖所得及回流文物拍卖所得
     */
    NOP6("","0904 财产拍卖所得及回流文物拍卖所得"),
    /**
     * 0801 个人房屋出租所得
     */
    NOP7("","0801 个人房屋出租所得"),
    /**
     * 0899 其他财产租赁所得
     */
    NOP8("","0899 其他财产租赁所得"),
    /**
     * 0110 企业年金
     */
    NOP9("","0110 企业年金"),
    /**
     * 0111 提前退休一次性补贴
     */
    NOP10("","0111 提前退休一次性补贴"),
    /**
     * 0901 股票转让所得
     */
    NOP11("","0901 股票转让所得"),
    /**
     * 0902 个人房屋转让所得
     */
    NOP12("","0902 个人房屋转让所得"),
    /**
     * 0999 其他财产转让所得
     */
    NOP13("","0999 其他财产转让所得"),
    /**
     * 0905 股权转让所得
     */
    NOP14("","0905 股权转让所得");

    private String  code;

    private String  desc;

    private IncomeSubjectEnumsAboutSzOffline(String code ,String desc)
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
