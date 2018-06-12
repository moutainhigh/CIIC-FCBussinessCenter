package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 个税所得项目
 * @author wuhua
 */
public enum IncomeSubject {

    /**
     * 正常工资薪金
     */
    NORMALSALARY("01","正常工资薪金"),
    /**
     * 外籍人员正常工资薪金
     */
    FOREIGNNORMALSALARY("02","外籍人员正常工资薪金"),
    /**
     * 全年一次性奖金收入
     */
    BONUSINCOMEYEAR("03","全年一次性奖金收入"),
    /**
     * 偶然所得
     */
    FORTUITOUSINCOME("04","偶然所得"),
    /**
     * 利息、股息、红利所得
     */
    IDDINCOME("05","利息、股息、红利所得"),
    /**
     * 劳务报酬所得
     */
    LABORINCOME("06","劳务报酬所得"),
    /**
     * 解除劳动合同一次性补偿金
     */
    LABORCONTRACT("07","解除劳动合同一次性补偿金"),
    /**
     * 个人股票期权行权收入
     */
    STOCKOPTIONINCOME("08","个人股票期权行权收入");

    private String  code;

    private String  desc;

    private IncomeSubject(String code ,String desc)
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
