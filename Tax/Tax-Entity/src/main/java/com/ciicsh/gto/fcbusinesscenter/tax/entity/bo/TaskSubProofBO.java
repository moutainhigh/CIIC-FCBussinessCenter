package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author yuantongqing
 */
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
     * 状态(中文)
     */
    private String statusName;

    /**
     * 城市
     */
    private String city;

    /**
     * 税务机构
     */
    private String taxOrganization;
    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;

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
    private LocalDate period;

    /**
     * 任务类型(01:自动,02:人工)
     */
    private String taskType;

    /**
     * 是否为合并任务
     */
    private Boolean isCombined;

    /**
     * 管理方编号(管理方切换)
     */
    private String[] managerNos;
    /**
     * 创建人displayname
     */
    private String createdByDisplayName;

    /**
     * 修改人displayname
     */
    private String modifiedByDisplayName;

    /**
     * 申报账户(中文)
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String[] getManagerNos() {
        return managerNos;
    }

    public void setManagerNos(String[] managerNos) {
        this.managerNos = managerNos;
    }

    public String getCreatedByDisplayName() {
        return createdByDisplayName;
    }

    public void setCreatedByDisplayName(String createdByDisplayName) {
        this.createdByDisplayName = createdByDisplayName;
    }

    public String getModifiedByDisplayName() {
        return modifiedByDisplayName;
    }

    public void setModifiedByDisplayName(String modifiedByDisplayName) {
        this.modifiedByDisplayName = modifiedByDisplayName;
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
        return "TaskSubProofBO{" +
                "id=" + id +
                ", taskNo='" + taskNo + '\'' +
                ", taskMainProofId=" + taskMainProofId +
                ", declareAccount='" + declareAccount + '\'' +
                ", headcount=" + headcount +
                ", chineseNum=" + chineseNum +
                ", foreignerNum=" + foreignerNum +
                ", status='" + status + '\'' +
                ", statusName='" + statusName + '\'' +
                ", city='" + city + '\'' +
                ", taxOrganization='" + taxOrganization + '\'' +
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
                ", managerNos=" + Arrays.toString(managerNos) +
                ", createdByDisplayName='" + createdByDisplayName + '\'' +
                ", modifiedByDisplayName='" + modifiedByDisplayName + '\'' +
                ", declareAccountName='" + declareAccountName + '\'' +
                ", station='" + station + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
