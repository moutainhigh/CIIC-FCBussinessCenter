package com.ciicsh.zorro.leopardwebservice.entity.enums;

/**
 * Created by jiangtianning on 2017/9/8.
 */
public enum DataTypeEnum implements IValuedEnum {
    TEXT(1),  // 文本
    NUM(2),  // 数字
    DATE(3),  // 日期
    BOOLEAN(4); // 布尔

    private int value;

    DataTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
