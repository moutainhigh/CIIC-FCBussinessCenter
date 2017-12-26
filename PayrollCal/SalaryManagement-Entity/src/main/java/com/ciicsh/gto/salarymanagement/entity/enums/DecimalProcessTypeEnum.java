package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by jiangtianning on 2017/9/8.
 * @author jiangtianning
 */
public enum DecimalProcessTypeEnum implements IValuedEnum {
    /**
     * 四舍五入
     */
    ROUND(1, "四舍五入"),

    /**
     * 简单去位
     */
    ROUND_DOWN(2, "简单去位");

    private int value;

    private String label;

    DecimalProcessTypeEnum(int value, String label) {
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
        for(DecimalProcessTypeEnum e : DecimalProcessTypeEnum.values()){
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }
}
