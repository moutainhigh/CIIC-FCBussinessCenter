package com.ciicsh.gto.fcbusinesscenter.util.entity;

/**
 * Created by bill on 18/6/4.
 */
public enum BatchTypeEnum {

    NORMAL(1),
    ADJUST(2),
    BACK_TRACE(3);

    private int value;

    private BatchTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
