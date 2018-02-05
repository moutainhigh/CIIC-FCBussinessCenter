package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by NeoJiang on 2018/2/1.
 *
 * @author NeoJiang
 */
public enum BizTypeEnum implements ValuedEnum{

    PR_GROUP_TEMPLATE(1, "薪资组模版"),

    PR_GROUP(2, "薪资组"),

    NORMAL_BATCH(3, "正常批次"),

    ADJUST_BATCH(4, "调整批次"),

    BACK_TRACE_BATCH(5, "回溯批次");

    private int value;

    private String label;

    BizTypeEnum(int value, String label){
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
