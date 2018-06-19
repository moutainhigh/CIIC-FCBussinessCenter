package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.frombatch;

import java.math.BigDecimal;
import java.util.Date;

public class MainBO {

    /**
     * 引擎计算批次号
     */
    private String batchNo;
    /**
     * 管理方编号
     */
    private String managerNo;
    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 个税总金额
     */
    private BigDecimal taxAmount;
    /**
     * 总人数
     */
    private Integer headcount;
    /**
     * 中方人数
     */
    private Integer chineseNum;
    /**
     * 外方人数
     */
    private Integer foreignerNum;
    /**
     * 薪资期间
     */
    private Date incomeYearMonth;
    /**
     * 批次状态
     */
    private String status;
    /**
     * 版本号
     */
    private int versionNo;

    /**
     * 批次类型
     */
    private String batchType;

    /**
     * 相关批次号
     */
    private String batchRefId;

    public String getBatchRefId() {
        return batchRefId;
    }

    public void setBatchRefId(String batchRefId) {
        this.batchRefId = batchRefId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public Date getIncomeYearMonth() {
        return incomeYearMonth;
    }

    public void setIncomeYearMonth(Date incomeYearMonth) {
        this.incomeYearMonth = incomeYearMonth;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public Integer getChineseNum() {
        return chineseNum;
    }

    public void setChineseNum(Integer chineseNum) {
        this.chineseNum = chineseNum;
    }

    public Integer getForeignerNum() {
        return foreignerNum;
    }

    public void setForeignerNum(Integer foreignerNum) {
        this.foreignerNum = foreignerNum;
    }
}
