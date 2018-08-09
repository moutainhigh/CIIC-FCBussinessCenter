package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by bill on 17/12/11.
 */
public enum  OperateTypeEnum implements ValuedEnum {

    ADD("添加",1),

    UPDATE("更新", 2),

    DELETE("删除", 3),

    SEARCH("查询", 4),

    IMPORT("导入", 5),
    ;

    private int val;
    private String label;

    OperateTypeEnum(String label, int val){
        this.val = val;
        this.label = label;
    }

    @Override
    public int getValue() {
        return this.val;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
