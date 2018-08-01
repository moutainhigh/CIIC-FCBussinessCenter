package com.ciicsh.gto.salarymanagement.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 正常批次主表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_normal_batch")
public class PrNormalBatchPO extends Model<PrNormalBatchPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 正常批次编号： 客户ID-计算日期-4位序号
     */
    private String code;
    /**
     * 所属管理方ID
     */
    @TableField("management_id")
    private String managementId;

    /**
     * 所属管理方名称
     */
    @TableField("management_name")
    private String managementName;
    /**
     * 是否计划批次
     */
    @TableField("is_scheduled")
    private Boolean isScheduled;
    /**
     * 计划批次设置, JSON格式字符串
     */
    @TableField("schedule_setting")
    private String scheduleSetting;
    /**
     * 薪资账套ID
     */
    @TableField("account_set_code")
    private String accountSetCode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 批次状态：
     * 1-新建
     * 2-计算中
     * 3-计算完成
     * 4-审核中
     * 5-审核完成
     * 6-关账
     * 7-已发放
     * 8-个税已申报
     */
    private Integer status;
    /**
     * 薪资期间 年/月
     */
    @TableField("period")
    private String period;

    /*实际计薪期间*/
    @TableField("actual_period")
    private String actualPeriod;

    //薪资开始日期
    @TableField("start_date")
    private String startDate;

    //薪资结束日期
    @TableField("end_date")
    private String endDate;

    @TableField("result_data")
    private String resultData;
    /**
     * 是否实施计算
     */
    @TableField("is_impl_cal")
    private Boolean isImplCal;
    /**
     * 是否有效
     */
    @TableField("is_active")
    private Boolean isActive;
    /**
     * 数据创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 最后修改时间
     */
    @TableField("modified_time")
    private Date modifiedTime;
    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;
    /**
     * 最后修改人
     */
    @TableField("modified_by")
    private String modifiedBy;

    /*是否垫付：0 表示未垫付，1表示已经垫付*/
    @TableField("has_advance")
    private int hasAdvance;

    /*是否来款：0表示未来款，1表示已来款*/
    @TableField("has_money")
    private boolean hasMoney;

    /*周期垫付日*/
    @TableField("advance_day")
    private int advanceDay;

    /*逾期垫付日期*/
    @TableField("advance_period")
    private String advancePeriod;
    /**
     * 表示是否为测试批次：1 表示 是； 0 表示 否；
     */
    @TableField("is_test_batch")
    private Integer isTestBatch;

    public String getAdvancePeriod() {
        return advancePeriod;
    }

    public void setAdvancePeriod(String advancePeriod) {
        this.advancePeriod = advancePeriod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public Boolean getScheduled() {
        return isScheduled;
    }

    public void setScheduled(Boolean isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getScheduleSetting() {
        return scheduleSetting;
    }

    public void setScheduleSetting(String scheduleSetting) {
        this.scheduleSetting = scheduleSetting;
    }

    public String getAccountSetCode() {
        return accountSetCode;
    }

    public void setAccountSetCode(String accountSetCode) {
        this.accountSetCode = accountSetCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public Boolean getImplCal() {
        return isImplCal;
    }

    public void setImplCal(Boolean isImplCal) {
        this.isImplCal = isImplCal;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getActualPeriod() {
        return actualPeriod;
    }

    public void setActualPeriod(String actualPeriod) {
        this.actualPeriod = actualPeriod;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getManagementName() {
        return managementName;
    }

    public void setManagementName(String managementName) {
        this.managementName = managementName;
    }

    public int getHasAdvance() {
        return hasAdvance;
    }

    public void setHasAdvance(int hasAdvance) {
        this.hasAdvance = hasAdvance;
    }

    public boolean getHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(Boolean hasMoney) {
        this.hasMoney = hasMoney;
    }

    public int getAdvanceDay() {
        return advanceDay;
    }

    public void setAdvanceDay(int advanceDay) {
        this.advanceDay = advanceDay;
    }

    public Integer getIsTestBatch() {
        return isTestBatch;
    }

    public void setIsTestBatch(Integer isTestBatch) {
        this.isTestBatch = isTestBatch;
    }

    @Override
    public String toString() {
        return "PrNormalBatchPO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", managementId='" + managementId + '\'' +
                ", managementName='" + managementName + '\'' +
                ", isScheduled=" + isScheduled +
                ", scheduleSetting='" + scheduleSetting + '\'' +
                ", accountSetCode='" + accountSetCode + '\'' +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", period='" + period + '\'' +
                ", actualPeriod='" + actualPeriod + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", resultData='" + resultData + '\'' +
                ", isImplCal=" + isImplCal +
                ", isActive=" + isActive +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", hasAdvance=" + hasAdvance +
                ", hasMoney=" + hasMoney +
                ", advanceDay=" + advanceDay +
                ", advancePeriod='" + advancePeriod + '\'' +
                ", isTestBatch=" + isTestBatch +
                "} " + super.toString();
    }
}
