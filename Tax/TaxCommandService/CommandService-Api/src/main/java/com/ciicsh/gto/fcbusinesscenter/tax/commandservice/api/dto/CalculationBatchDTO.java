package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;


/**
 * @author wuhua
 */
public class CalculationBatchDTO {

    private String managerName;
    private String managerNo;
    private String batchNo;
    private String[] batchIds;
    private String[] batchNos;
    private Integer currentNum;
    private Integer pageSize;

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String[] getBatchIds() {
        return batchIds;
    }

    public String[] getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(String[] batchNos) {
        this.batchNos = batchNos;
    }

    public void setBatchIds(String[] batchIds) {
        this.batchIds = batchIds;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "CalculationBatchDTO{" +
                "managerName='" + managerName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
