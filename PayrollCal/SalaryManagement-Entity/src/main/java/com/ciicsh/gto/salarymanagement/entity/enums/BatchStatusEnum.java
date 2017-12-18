package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by bill on 17/12/8.
 * 批次状态：
 */
public enum BatchStatusEnum implements IValuedEnum {
    /**
     * 新建
     */
    NEW(1, "新建"),

    /**
     * 审核中
     */
    COMPUTING(2, "计算中"),

    /**
     * 计算完成
     */
    COMPUTED(3, "计算完成"),

    /**
     * 待审核
     */
    PENDING(4, "待审核"),

    /**
     * 审核完成
     */
    APPROVAL(5, "审核完成"),

    /**
     * 审核拒绝
     */
    REJECT(6, "审核拒绝"),

    /**
     * 关账
     */
    CLOSED(7, "关账"),

    /**
     * 已发放
     */
    ISSUED(8, "薪资已发放"),

    /**
     * 个税已申报
     */
    TAX_DECLARED(9, "个税已申报"),

    ;


    private int value;
    private String label;

    BatchStatusEnum(int value, String lable){
        this.value = value;
        this.label = lable;
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
