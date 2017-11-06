package com.ciicsh.zorro.leopardwebservice.entity.enums;

/**
 * Created by jiangtianning on 2017/9/8.
 */
public enum PayItemTypeEnum implements IValuedEnum {
    INPUT(1), //输入项
    FIX(2), //固定项
    CALC(3); //计算项

    private int value;

    PayItemTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
