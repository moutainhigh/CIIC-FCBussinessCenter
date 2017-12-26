package com.ciicsh.gto.fcsupportcenter.tax.entity.bo;

import java.util.Date;

/**
 * @author yuantongqing on 2017/12/13
 */
public class TaskMainProofBO {

    private Long id;
    private String taskNo;
    private String managerNo;
    private String managerName;
    private Integer headcount;
    private Integer chineseNum;
    private Integer foreignerNum;
    private String createdBy;
    private Date createdTime;
    private String status;
    private Boolean isActive;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "TaskMainProofBO{" +
                "id=" + id +
                ", taskNo='" + taskNo + '\'' +
                ", managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", status='" + status + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
