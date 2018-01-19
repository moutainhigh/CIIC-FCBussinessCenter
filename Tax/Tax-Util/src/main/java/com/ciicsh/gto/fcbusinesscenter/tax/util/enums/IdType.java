package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 证件类型
 * @author wuhua
 */
public enum IdType {

    /**
     * 居民身份证
     */
    IT01("居民身份证"),
    /**
     * 军官证
     */
    IT02("军官证"),
    /**
     * 士兵证
     */
    IT03("士兵证"),
    /**
     * 港澳居民来往内地通行证
     */
    IT04("港澳居民来往内地通行证"),
    /**
     * 台湾居民来往大陆通行证
     */
    IT05("台湾居民来往大陆通行证"),
    /**
     * 中国护照
     */
    IT06("中国护照"),
    /**
     * 外国护照
     */
    IT07("外国护照");

    private String  message;

    private IdType(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
