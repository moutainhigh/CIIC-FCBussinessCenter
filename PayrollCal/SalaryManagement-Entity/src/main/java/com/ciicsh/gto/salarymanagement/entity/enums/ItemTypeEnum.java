package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by jiangtianning on 2017/9/8.
 */
public enum ItemTypeEnum implements IValuedEnum {

    /**
     * 输入项
     */
    INPUT(1, "输入项"),

    /**
     * 固定项
     */
    FIX(2, "固定项"),

    /**
     * 计算项
     */
    CALC(3, "计算项");

    private int value;

    private String label;

    ItemTypeEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public static String getLabelByValue(int value) {
        for(ItemTypeEnum e : ItemTypeEnum.values()){
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }
}
