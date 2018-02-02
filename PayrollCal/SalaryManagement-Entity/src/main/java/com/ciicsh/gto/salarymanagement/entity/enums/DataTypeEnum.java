package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by jiangtianning on 2017/9/8.
 * @author jiangtianning
 */
public enum DataTypeEnum implements ValuedEnum {
    /**
     * 文本
     */
    TEXT(1, "文本"),

    /**
     * 数字
     */
    NUM(2, "数字"),

    /**
     * 日期
     */
    DATE(3, "日期"),

    /**
     * 布尔
     */
    BOOLEAN(4, "布尔");

    private int value;

    private String label;

    DataTypeEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getLabel() { return label; }

    public static String getLabelByValue(int value) {
        for(DataTypeEnum e : DataTypeEnum.values()){
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }
}
