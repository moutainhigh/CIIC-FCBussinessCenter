package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author yuantongqing on 2018/01/24
 */
public enum TaskStatus {
    /**
     * 草稿
     */
    TS00("草稿"),
    /**
     * 已提交
     */
    TS01("已提交"),
    /**
     * 通过
     */
    TS02("通过"),
    /**
     * 退回
     */
    TS03("退回"),
    /**
     * 已完成
     */
    TS04("已完成"),
    /**
     * 失效
     */
    TS05("失效");

    private String  message;

    private TaskStatus(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
