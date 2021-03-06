package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.util.Date;

/**
 * @author yuantongqing on 2017/12/12
 */
public class TaskSubProofDTO{

    private Long id;

    private String taskNo;

    private Long taskMainProofId;

    private String declareAccount;

    private String city;

    private String taxOrganization;

    private Integer headcount;

    private Integer chineseNum;

    private Integer foreignerNum;

    private String sendStatus;

    /**
     *  状态
     */
    private String status;

    /**
     * 状态(中文)
     */
    private String statusName;

    private String createdBy;

    private Date createdTime;
    /**
     * 管理方编号
     */
    private String managerNo;
    /**
     * 管理方名称
     */
    private String managerName;

    /**
     * 申报账户名称
     */
    private String declareAccountName;

    /**
     * 所属税务局
     */
    private String station;

    /**
     * 城市代码
     */
    private String cityCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskMainProofId() {
        return taskMainProofId;
    }

    public void setTaskMainProofId(Long taskMainProofId) {
        this.taskMainProofId = taskMainProofId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTaxOrganization() {
        return taxOrganization;
    }

    public void setTaxOrganization(String taxOrganization) {
        this.taxOrganization = taxOrganization;
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

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getDeclareAccountName() {
        return declareAccountName;
    }

    public void setDeclareAccountName(String declareAccountName) {
        this.declareAccountName = declareAccountName;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "TaskSubProofDTO{" +
                "id=" + id +
                ", taskNo='" + taskNo + '\'' +
                ", taskMainProofId=" + taskMainProofId +
                ", declareAccount='" + declareAccount + '\'' +
                ", city='" + city + '\'' +
                ", taxOrganization='" + taxOrganization + '\'' +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                ", sendStatus='" + sendStatus + '\'' +
                ", status='" + status + '\'' +
                ", statusName='" + statusName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", declareAccountName='" + declareAccountName + '\'' +
                ", station='" + station + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
