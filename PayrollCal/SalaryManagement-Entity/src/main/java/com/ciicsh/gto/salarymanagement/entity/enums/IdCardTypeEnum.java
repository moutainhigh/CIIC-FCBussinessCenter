package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by NeoJiang on 2018/4/2.
 */
public enum IdCardTypeEnum implements ValuedEnum {
    身份证(1, "身份证"),
    护照(2, "护照"),
    军警官证(3, "军(警)官证"),
    士兵证(4, "士兵证"),
    台胞证(5, "台胞证"),
    回乡证(6, "回乡证"),
    其他(7, "其他");

    private int value;

    private String label;

    IdCardTypeEnum(int value, String label) {
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
