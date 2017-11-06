package com.ciicsh.zorro.leopardwebservice.entity.enums;

/**
 * Created by jiangtianning on 2017/9/8.
 */
public enum DecimalProcessTypeEnum implements IValuedEnum {
    ROUND(1), //四舍五入
    ROUND_DOWN(2); //简单去位

    private int value;

    DecimalProcessTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
