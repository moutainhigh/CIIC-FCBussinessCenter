package com.ciicsh.gto.fcsupportcenter.tax.entity.bo;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.Date;

public class TaskSubProofBO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 完税凭证主任务ID
     */
    private Long taskMainProofId;

    /**
     * 申报账户
     */
    private String declareAccount;

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
     * 任务状态
     */
    private String status;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 修改时间
     */
    private Date modifiedTime;

    /**
     * 是否可用
     */
    private Boolean isActive;

    /**
     * 管理方编号
     */
    private String managerNo;
    /**
     * 管理房名称
     */
    private String managerName;

    /**
     * 个税期间
     */
    private Date  period;

    /**
     * 任务类型(01:自动,02:人工)
     */
    private String taskType;

    /**
     * 是否为合并任务
     */
    private Boolean isCombined;

    /**
     * 分页page对象
     */
    private Page page;

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

    public Long getTaskMainProofId() {
        return taskMainProofId;
    }

    public void setTaskMainProofId(Long taskMainProofId) {
        this.taskMainProofId = taskMainProofId;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public Boolean getCombined() {
        return isCombined;
    }

    public void setCombined(Boolean combined) {
        isCombined = combined;
    }

    @Override
    public String toString() {
        return "TaskSubProofBO{" +
                "id=" + id +
                ", taskNo='" + taskNo + '\'' +
                ", taskMainProofId=" + taskMainProofId +
                ", declareAccount='" + declareAccount + '\'' +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                ", status='" + status + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedTime=" + modifiedTime +
                ", isActive=" + isActive +
                ", managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", period=" + period +
                ", taskType='" + taskType + '\'' +
                ", isCombined=" + isCombined +
                ", page=" + page +
                '}';
    }
}
