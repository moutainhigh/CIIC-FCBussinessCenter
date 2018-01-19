package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author yuantongqing on 2018/01/19
 */
public enum BusinessStatusType {

    /**
     * 处理中
     */
    HANDLING("21"),
    /**
     * 被退回
     */
    COMPLETED("22"),
    /**
     * 已失效
     */
    RETREATED("23"),
    /**
     * 已失效
     */
    FAILED("24");

    private String  message;

    private BusinessStatusType(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
