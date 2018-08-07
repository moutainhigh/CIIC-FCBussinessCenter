package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by NeoJiang on 2018/5/5.
 */
public enum InWitchCompareBatchEnum implements ValuedEnum {

    ALL(0, "都存在"),
    TGT_ONLY(1, "只存在于对比批次1"),
    TGTTWO_ONLY(2, "只存在于对比批次2"),
    SRC_ONLY(9, "只存在于源批次"),
    SRC_TGT(91, "存在于源批次和对比批次1"),
    SRC_TGTTWO(92, "存在于源批次和对比批次2");

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
    public String getLabel() {
        return label;
    }
}
