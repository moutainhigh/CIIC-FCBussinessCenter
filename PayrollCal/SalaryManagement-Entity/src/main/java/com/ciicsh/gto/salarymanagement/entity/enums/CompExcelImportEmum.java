package com.ciicsh.gto.salarymanagement.entity.enums;

/**
 * Created by bill on 18/1/2.
 */

/**
 * 薪资计算：雇员数据导入规则
 */
public enum CompExcelImportEmum implements ValuedEnum {
    OVERRIDE_EXPORT("覆盖导入",1),
    MODIFY_EXPORT("修改导入",2),
    APPEND_EXPORT("追加导入",3),
    DIFF_EXPORT("差异导入",4),
    ;

    private String label;
    private int val;

    @Override
    public int getValue() {
        return this.val;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    CompExcelImportEmum(String label, int val){
        this.label = label;
        this.val = val;
    }
}
