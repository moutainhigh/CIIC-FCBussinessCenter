package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author yuantongqing on 2018/01/19
 */
public enum BusinessStatusType {

    /**
     * 通过/待处理(申报)
     */
    DECLARE_PASSED("02"),
    /**
     * 退回(申报)
     */
    DECLARE_RETREATED("03"),
    /**
     * 已完成(申报)
     */
    DECLARE_COMPLETED("04"),
    /**
     * 失效(申报)
     */
    DECLARE_FAILED("05"),

    /**
     * 通过/待处理(划款)
     */
    MONEY_PASSED("02"),
    /**
     * 退回(划款)
     */
    MONEY_RETREATED("03"),
    /**
     * 已完成(划款)
     */
    MONEY_COMPLETED("04"),
    /**
     * 失效(划款)
     */
    MONEY_FAILED("05"),

    /**
     * 通过/待处理(缴纳)
     */
    PAY_PASSED("02"),
    /**
     * 退回(缴纳)
     */
    PAY_RETREATED("03"),
    /**
     * 已完成(缴纳)
     */
    PAY_COMPLETED("04"),
    /**
     * 失效(缴纳)
     */
    PAY_FAILED("05");

    private String  message;

    private BusinessStatusType(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
