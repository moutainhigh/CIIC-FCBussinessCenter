package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

public enum Gender {

    MALE("1","男"),

    FEMALE("0","女");

    private String  code;

    private String  desc;

    private Gender(String code ,String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
