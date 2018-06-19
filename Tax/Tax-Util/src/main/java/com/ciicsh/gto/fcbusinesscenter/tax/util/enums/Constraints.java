package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

public enum Constraints {

    C1(1,"批次已取消关账");

    private  int  code;

    private String  desc;

    private Constraints(int code, String desc)
    {

        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
