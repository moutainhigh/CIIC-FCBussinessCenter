package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by bill on 17/12/9.
 * 薪资期间类型：本月、上月、下月
 */
public enum PeriodTypeEnum implements ValuedEnum {

    THIS_MONTH("本月",0),

    LAST_MONTH("上月",1),

    NEXT_MONTH("下月",2)

    ;

    private String label;

    private int value;

    PeriodTypeEnum(String label, int value){
        this.label = label;
        this.value = value;
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
