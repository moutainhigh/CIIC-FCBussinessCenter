package com.ciicsh.gto.salarymanagement.entity.bo;

/**
 * Created by bill on 18/5/18.
 */
public class ExcelItemBO {
    private String groupValues;
    private String groupColumns;
    private int rowIndex;

    public String getGroupColumns() {
        return groupColumns;
    }

    public void setGroupColumns(String groupColumns) {
        this.groupColumns = groupColumns;
    }

    public String getGroupValues() {
        return groupValues;
    }

    public void setGroupValues(String groupValues) {
        this.groupValues = groupValues;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
