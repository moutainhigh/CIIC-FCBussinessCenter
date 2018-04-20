package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;


import java.util.Date;

/**
 * @author yuantongqing
 * data 2017/12/8
 */
public class TaskMainProofDTO extends BaseDTO{
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 管理房编号
     */
    private String managerNo;
    /**
     * 管理方名称
     */
    private String managerName;
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
     * 创建人
     */
    private String createdBy;
    /**
     * 起始日期
     */
    private String submitTimeStart;
    /**
     * 结束日期
     */
    private String submitTimeEnd;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 当前页数
     */
    private Integer currentNum;
    /**
     * 每页显示条目
     */
    private Integer pageSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSubmitTimeStart() {
        return submitTimeStart;
    }

    public void setSubmitTimeStart(String submitTimeStart) {
        this.submitTimeStart = submitTimeStart;
    }

    public String getSubmitTimeEnd() {
        return submitTimeEnd;
    }

    public void setSubmitTimeEnd(String submitTimeEnd) {
        this.submitTimeEnd = submitTimeEnd;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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
        return "TaskMainProofDTO{" +
                "id=" + id +
                ", taskNo='" + taskNo + '\'' +
                ", managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                ", createdBy='" + createdBy + '\'' +
                ", submitTimeStart='" + submitTimeStart + '\'' +
                ", submitTimeEnd='" + submitTimeEnd + '\'' +
                ", createdTime=" + createdTime +
                ", currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
