package com.ciicsh.gto.salarymanagement.entity.dto;

/**
 * Created by bill on 18/5/15.
 */
public class ExcelMapDTO {

    private String[] excelCols;
    private String[] payItemNames;

    public String[] getExcelCols() {
        return excelCols;
    }

    public void setExcelCols(String[] excelCols) {
        this.excelCols = excelCols;
    }

    public String[] getPayItemNames() {
        return payItemNames;
    }

    public void setPayItemNames(String[] payItemNames) {
        this.payItemNames = payItemNames;
    }
}
