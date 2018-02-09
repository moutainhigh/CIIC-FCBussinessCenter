package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;


import java.util.Arrays;

/**
 * @author wuhua
 */
public class TaskSubDeclareDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 缴纳账户
     */
    private String declareAccount;

    /**
     * 管理方名称
     */
    private String managerName;

    private String batchNo;
    /**
     * 页签类型：currentPan,currentBeforePan,currentAfterPan
     */
    private String periodType;

    /**
     * 当前页数
     */
    private Integer currentNum;
    /**
     * 每页显示条目
     */
    private Integer pageSize;

    /**
     * 批量完成/批量退回申报子任务ID
     */
    private String[] subDeclareIds;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
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

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
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

    public String[] getSubDeclareIds() {
        return subDeclareIds;
    }

    public void setSubDeclareIds(String[] subDeclareIds) {
        this.subDeclareIds = subDeclareIds;
    }

    @Override
    public String toString() {
        return "TaskSubDeclareDTO{" +
                "id=" + id +
                ", declareAccount='" + declareAccount + '\'' +
                ", managerName='" + managerName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", periodType='" + periodType + '\'' +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", subDeclareIds=" + Arrays.toString(subDeclareIds) +
                '}';
    }
}
