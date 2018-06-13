package com.ciicsh.gto.fcbusinesscenter.tax.util.enums.declare.offline.gd;

/**
 * 广东线下所得项目
 * @author yuantongqing on 2018-06-05
 */
public enum IncomeSubjectEnumsAboutGdOffline {
    /**
     * _0101_正常工资薪金
     */
    NORMALSALARY("01","_0101_正常工资薪金"),
    /**
     * _0102_外籍人员正常工资薪金
     */
    FOREIGNNORMALSALARY("02","_0102_外籍人员正常工资薪金"),
    /**
     * _0103_全年一次性奖金收入
     */
    BONUSINCOMEYEAR("03","_0103_全年一次性奖金收入"),
    /**
     * _1000_偶然所得
     */
    FORTUITOUSINCOME("04","_1000_偶然所得"),
    /**
     * _0700_利息、股息、红利所得
     */
    IDDINCOME("05","_0700_利息、股息、红利所得"),
    /**
     * _0400_劳务报酬所得
     */
    LABORINCOME("06","_0400_劳务报酬所得"),
    /**
     * _0108_解除劳动合同一次性补偿金
     */
    LABORCONTRACT("07","_0108_解除劳动合同一次性补偿金"),
    /**
     * _0109_个人股票期权行权收入
     */
    STOCKOPTIONINCOME("08","_0109_个人股票期权行权收入"),
    /**
     * _0104_外籍人员数月奖金
     */
    NOP1("","_0104_外籍人员数月奖金"),
    /**
     * _0107_内退一次性补偿金
     */
    NOP2("","_0107_内退一次性补偿金"),
    /**
     * _0111_提前退休一次性补贴
     */
    NOP3("","_0111_提前退休一次性补贴"),
    /**
     * _0901_股票转让所得
     */
    NOP4("","_0901_股票转让所得"),
    /**
     * _0902_个人房屋转让所得
     */
    NOP5("","_0902_个人房屋转让所得"),
    /**
     * _0905_股权转让所得
     */
    NOP6("","_0905_股权转让所得"),
    /**
     * _0999_其他财产转让所得
     */
    NOP7("","_0999_其他财产转让所得"),
    /**
     * _0904_财产拍卖所得及回流文物拍卖所得
     */
    NOP8("","_0904_财产拍卖所得及回流文物拍卖所得"),
    /**
     * _0801_个人房屋出租所得
     */
    NOP9("","_0801_个人房屋出租所得"),
    /**
     * _0899_其他财产租赁所得
     */
    NOP10("","_0899_其他财产租赁所得"),
    /**
     * _0500_稿酬所得
     */
    NOP11("","_0500_稿酬所得"),
    /**
     * _0600_特许权使用费所得
     */
    NOP12("","_0600_特许权使用费所得"),
    /**
     * _9900_其他所得
     */
    NOP13("","_9900_其他所得");

    private String  code;

    private String  desc;

    private IncomeSubjectEnumsAboutGdOffline(String code ,String desc)
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
