package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by jiangtianning on 2017/12/6.
 * @author jiangtianning
 */
public enum ApprovalStatusEnum implements IValuedEnum{

    /**
     * 草稿
     */
    DRAFT(0, "草稿"),

    /**
     * 审核中
     */
    AUDITING(1, "待审核"),

    /**
     * 审核完成
     */
    APPROVE(2, "审核完成"),

    /**
     * 拒绝
     */
    DENIED(3, "拒绝");

    private int value;

    private String label;

    ApprovalStatusEnum(int value, String label) {
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
        for(ApprovalStatusEnum e : ApprovalStatusEnum.values()){
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }
}
