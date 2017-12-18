package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by bill on 17/12/8.
 * 批次类型
 */
public enum BatchTypeEnum implements IValuedEnum {

    NORMAL(1, "正常批次"),

    ADJUST(2, "调整批次"),

    BACK(3, "回溯批次"),
    ;

    private int value;

    private String label;

    BatchTypeEnum(int value, String label){
        this.value = value;
        this.label = label;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
