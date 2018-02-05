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
     * 处理中
     */
    TS01("处理中"),
    /**
     * 被退回
     */
    TS02("被退回"),
    /**
     * 已完成
     */
    TS03("已完成"),
    /**
     * 已失效
     */
    TS04("已失效");

    private String  message;

    private TaskStatus(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
