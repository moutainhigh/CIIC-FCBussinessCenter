package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * 薪酬计算批次状态
 * @author wuhua
 */
public enum BatchNoStatus {

    /**
     * 已关账
     */
    BNS00("已关账");

    private String  message;

    private BatchNoStatus(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
