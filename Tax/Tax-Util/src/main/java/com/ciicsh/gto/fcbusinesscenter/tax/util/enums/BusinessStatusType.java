package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 页面tabs标签的name和任务的status枚举
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
    FAILED("04"),

    /**
     * 审核中(主任务)
     */
    TASKMAIN_CHECKING("01"),
    /**
     * 通过(主任务)
     */
    TASKMAIN_PASSED("02"),
    /**
     * 退回(主任务)
     */
    TASKMAIN_RETREATED("03"),
    /**
     * 失效(主任务)
     */
    TASKMAIN_CANCELLED("05");

    private String  message;

    private BusinessStatusType(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
