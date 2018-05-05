package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by NeoJiang on 2018/5/5.
 */
public enum InWitchCompareBatchEnum implements ValuedEnum{

    BOTH(0, "都存在"),
    SRC(1, "源批次"),
    TGT(2, "对比批次");

    private int value;

    private String label;

    InWitchCompareBatchEnum(int value, String label) {
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
