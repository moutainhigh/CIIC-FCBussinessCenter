package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author yuantongqing on 2018/01/15
 */
public enum BusinessStatus {

    /**
     * 处理中
     */
    BS21("处理中"),
    /**
     * 被退回
     */
    BS22("被退回"),
    /**
     * 已完成
     */
    BS23("已完成"),
    /**
     * 已失效
     */
    BS24("已失效");

    private String  message;

    private BusinessStatus(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
