package com.ciicsh.gto.salarymanagement.entity.dto;

import java.util.Date;

/**
 * Created by bill on 18/5/16.
 */
public class PrBatchExcelMapDTO {

    private String batchCode;
    /**
     * 批次导入：薪资结构与excel列映射关系
     */
    private String mappingResult;
    /**
     * 唯一性验证映射
     */
    private String identityResult;

    private String excelCols;

    public String getExcelCols() {
        return excelCols;
    }

    public void setExcelCols(String excelCols) {
        this.excelCols = excelCols;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getMappingResult() {
        return mappingResult;
    }

    public void setMappingResult(String mappingResult) {
        this.mappingResult = mappingResult;
    }

    public String getIdentityResult() {
        return identityResult;
    }

    public void setIdentityResult(String identityResult) {
        this.identityResult = identityResult;
    }

}
