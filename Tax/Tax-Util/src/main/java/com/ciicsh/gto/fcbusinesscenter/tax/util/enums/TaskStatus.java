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
     * 待审批
     */
    TS01("待审批"),
    /**
     * 处理中
     */
    TS02("处理中"),
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
    TS05("失效"),
    /**
     * 部分完成
     */
    TS06("部分完成"),
    /**
     * 部分退回
     */
    TS07("部分退回");

    private String  message;

    private TaskStatus(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
