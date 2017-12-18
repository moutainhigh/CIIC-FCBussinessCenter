package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by jiangtianning on 2017/11/15.
 * @author jiangtianning
 */
public enum DefaultValueStyleEnum implements IValuedEnum  {
    /**
     * 使用历史数据
     */
    USE_HISTORY_DATA(1, "使用历史数据"),

    /**
     * 使用基本值
     */
    USE_BASIC_DATA(2, "使用基本值");

    private int value;

    private String label;

    DefaultValueStyleEnum(int value, String label) {
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
        for(DefaultValueStyleEnum e : DefaultValueStyleEnum.values()){
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }
}

