package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 证件类型
 * @author wuhua
 */
public enum IdType {

    IT01("居民身份证"),
    IT02("军官证"),
    IT03("士兵证"),
    IT04("港澳居民来往内地通行证"),
    IT05("台湾居民来往大陆通行证"),
    IT06("中国护照"),
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
