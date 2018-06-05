package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by bill on 18/5/31.
 */

//0表示未垫付，1 表示周期垫付，2表示偶然垫付，3表示水单垫付
public enum AdvanceEnum implements ValuedEnum {

    /**
     * 未垫付
     */
    NOADVANCE(0, "未垫付"),

    /**
     * 周期垫付
     */
    CIRCLE(1, "周期垫付"),

    /**
     * 偶然垫付
     */
    ACCIDENT(2, "偶然垫付"),

    /**
     * 水单垫付
     */
    SLIP(3, "水单垫付");

    private int value;

    private String label;

    AdvanceEnum(int value, String label) {
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
        for(AdvanceEnum e : AdvanceEnum.values()){
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }
}