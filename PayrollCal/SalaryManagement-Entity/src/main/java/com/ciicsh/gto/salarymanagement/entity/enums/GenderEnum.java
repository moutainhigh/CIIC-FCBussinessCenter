package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by jiangtianning on 2017/9/12.
 */
public enum GenderEnum implements ValuedEnum {
    其它(0, "其它"),
    男(1, "男"),
    女(2, "女");

    private int value;

    private String label;

    GenderEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getLabel() { return label; }
}
