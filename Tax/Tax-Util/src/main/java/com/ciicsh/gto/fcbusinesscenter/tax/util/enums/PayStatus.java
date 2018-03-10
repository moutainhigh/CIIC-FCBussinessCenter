package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author yuantongqing on 2018/03/06
 */
public enum PayStatus {
    /**
     * 未划款
     */
    PS00("未划款"),
    /**
     * 划款中
     */
    PS01("划款中"),
    /**
     * 已划款
     */
    PS02("已划款"),
    /**
     * 驳回
     */
    PS03("驳回");

    private String  message;

    private PayStatus(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
