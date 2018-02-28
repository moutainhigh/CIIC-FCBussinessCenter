package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author wuhua
 */
public enum SourceTypeForLog {
    /**
     * 个税数据
     */
    ST00("个税数据"),
    /**
     * 个税任务
     */
    ST01("个税任务"),
    /**
     * 申报
     */
    ST02("申报"),
    /**
     * 划款
     */
    ST03("划款"),
    /**
     * 缴纳
     */
    ST04("缴纳"),
    /**
     * 完税凭证
     */
    ST05("完税凭证");

    private String  message;

    private SourceTypeForLog(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
