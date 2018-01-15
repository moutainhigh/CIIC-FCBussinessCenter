package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author yuantongqing on 2018/01/15
 */
public enum VoucherStatus {

    VS00("草稿"),
    VS01("处理中"),
    VS02("被退回"),
    VS03("已完成"),
    VS04("已失效");

    private String  message;

    private VoucherStatus(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
