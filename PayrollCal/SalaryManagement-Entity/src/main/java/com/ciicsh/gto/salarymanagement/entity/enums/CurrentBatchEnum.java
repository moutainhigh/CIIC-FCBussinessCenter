package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018年8月2日15:20:00
 * @description 当前批次
 */
public enum CurrentBatchEnum implements ValuedEnum {

    SRC_BATCH(0, "源批次"),
    TGT_BATCH(1, "对比批次1"),
    TGTTWO_BATCH(2, "对比批次2");

    private int value;

    private String label;

    CurrentBatchEnum(int value, String label) {
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
        for(DataTypeEnum e : DataTypeEnum.values()){
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }

}
