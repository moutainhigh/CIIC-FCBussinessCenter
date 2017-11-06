package com.ciicsh.zorro.leopardwebservice.entity.enums;

/**
 * Created by jiangtianning on 2017/9/12.
 */
public enum GenderEnum implements IValuedEnum {
    其它(0),
    男(1),
    女(2);

    private int value;

    GenderEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
