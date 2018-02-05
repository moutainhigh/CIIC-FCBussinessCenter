package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author yuantongqing on 2018/01/19
 */
public enum BusinessStatusType {

    /**
     * 处理中
     */
    HANDLING("01"),
    /**
     * 已完成
     */
    COMPLETED("02"),
    /**
     * 已退回
     */
    RETREATED("03"),
    /**
     * 已失效
     */
    FAILED("04");

    private String  message;

    private BusinessStatusType(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
