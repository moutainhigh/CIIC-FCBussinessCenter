package com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.dto;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工作日历DTO
 * </p>
 *
 * @author guwei
 * @since 2017-12-27
 */
public class WorkingCalendarPageDTO extends Model<WorkingCalendarPageDTO> {

    private static final long serialVersionUID = 1L;

	/**
	 * 管理方名称
	 */
	private String managementName;

	public String getManagementName() {
		return managementName;
	}

	public void setManagementName(String managementName) {
		this.managementName = managementName;
	}

	/**
     * 工作日历id
     */
	private Integer cmyFcWorkingCalendarId;
    /**
     * 工作日历名称
     */
	private String workingCalendarName;
	/**
	 * 管理方id
	 */
	private String managementId;
    /**
     * 工作年份
     */
	private String year;
    /**
     * 一月工作日，以逗号分隔
     */
	private String workDayJanuary;
    /**
     * 二月工作日，以逗号分隔
     */
	private String workDayFebruary;
    /**
     * 四月工作日，以逗号分隔
     */
	private String workDayApril;
    /**
     * 三月工作日，以逗号分隔
     */
	private String workDayMarch;
    /**
     * 五月工作日，以逗号分隔
     */
	private String workDayMay;
    /**
     * 六月工作日，以逗号分隔
     */
	private String workDayJune;
    /**
     * 七月工作日，以逗号分隔
     */
	private String workDayJuly;
    /**
     * 八月工作日，以逗号分隔
     */
	private String workDaySeptember;
    /**
     * 九月工作日，以逗号分隔
     */
	private String workDayAugust;
    /**
     * 十月工作日，以逗号分隔
     */
	private String workDayOctober;
    /**
     * 十一月工作日，以逗号分隔
     */
	private String workDayNovember;
    /**
     * 十二月工作日，以逗号分隔
     */
	private String workDayDecember;
    /**
     * 一月休息日，以逗号分隔
     */
	private String restDayJanuary;
    /**
     * 二月休息日，以逗号分隔
     */
	private String restDayFebruary;
    /**
     * 三月休息日，以逗号分隔
     */
	private String restDayMarch;
    /**
     * 四月休息日，以逗号分隔
     */
	private String restDayApril;
    /**
     * 五月休息日，以逗号分隔
     */
	private String restDayMay;
    /**
     * 六月休息日，以逗号分隔
     */
	private String restDayJune;
    /**
     * 七月休息日，以逗号分隔
     */
	private String restDayJuly;
    /**
     * 八月休息日，以逗号分隔
     */
	private String restDaySeptember;
    /**
     * 九月休息日，以逗号分隔
     */
	private String restDayAugust;
    /**
     * 十月休息日，以逗号分隔
     */
	private String restDayOctober;
    /**
     * 十一月休息日，以逗号分隔
     */
	private String restDayNovember;
    /**
     * 十二月休息日，以逗号分隔
     */
	private String restDayDecember;
    /**
     * 是否有效
     */
	private Boolean isActive;
    /**
     * 创建时间
     */
	private Date createdTime;
    /**
     * 最后修改时间
     */
	private Date modifiedTime;
    /**
     * 创建人
     */
	private String createdBy;
    /**
     * 最后修改人
     */
	private String modifiedBy;


	public Integer getCmyFcWorkingCalendarId() {
		return cmyFcWorkingCalendarId;
	}

	public void setCmyFcWorkingCalendarId(Integer cmyFcWorkingCalendarId) {
		this.cmyFcWorkingCalendarId = cmyFcWorkingCalendarId;
	}

	public String getWorkingCalendarName() {
		return workingCalendarName;
	}

	public void setWorkingCalendarName(String workingCalendarName) {
		this.workingCalendarName = workingCalendarName;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWorkDayJanuary() {
		return workDayJanuary;
	}

	public void setWorkDayJanuary(String workDayJanuary) {
		this.workDayJanuary = workDayJanuary;
	}

	public String getWorkDayFebruary() {
		return workDayFebruary;
	}

	public void setWorkDayFebruary(String workDayFebruary) {
		this.workDayFebruary = workDayFebruary;
	}

	public String getWorkDayApril() {
		return workDayApril;
	}

	public void setWorkDayApril(String workDayApril) {
		this.workDayApril = workDayApril;
	}

	public String getWorkDayMarch() {
		return workDayMarch;
	}

	public void setWorkDayMarch(String workDayMarch) {
		this.workDayMarch = workDayMarch;
	}

	public String getWorkDayMay() {
		return workDayMay;
	}

	public void setWorkDayMay(String workDayMay) {
		this.workDayMay = workDayMay;
	}

	public String getWorkDayJune() {
		return workDayJune;
	}

	public void setWorkDayJune(String workDayJune) {
		this.workDayJune = workDayJune;
	}

	public String getWorkDayJuly() {
		return workDayJuly;
	}

	public void setWorkDayJuly(String workDayJuly) {
		this.workDayJuly = workDayJuly;
	}

	public String getWorkDaySeptember() {
		return workDaySeptember;
	}

	public void setWorkDaySeptember(String workDaySeptember) {
		this.workDaySeptember = workDaySeptember;
	}

	public String getWorkDayAugust() {
		return workDayAugust;
	}

	public void setWorkDayAugust(String workDayAugust) {
		this.workDayAugust = workDayAugust;
	}

	public String getWorkDayOctober() {
		return workDayOctober;
	}

	public void setWorkDayOctober(String workDayOctober) {
		this.workDayOctober = workDayOctober;
	}

	public String getWorkDayNovember() {
		return workDayNovember;
	}

	public void setWorkDayNovember(String workDayNovember) {
		this.workDayNovember = workDayNovember;
	}

	public String getWorkDayDecember() {
		return workDayDecember;
	}

	public void setWorkDayDecember(String workDayDecember) {
		this.workDayDecember = workDayDecember;
	}

	public String getRestDayJanuary() {
		return restDayJanuary;
	}

	public void setRestDayJanuary(String restDayJanuary) {
		this.restDayJanuary = restDayJanuary;
	}

	public String getRestDayFebruary() {
		return restDayFebruary;
	}

	public void setRestDayFebruary(String restDayFebruary) {
		this.restDayFebruary = restDayFebruary;
	}

	public String getRestDayMarch() {
		return restDayMarch;
	}

	public void setRestDayMarch(String restDayMarch) {
		this.restDayMarch = restDayMarch;
	}

	public String getRestDayApril() {
		return restDayApril;
	}

	public void setRestDayApril(String restDayApril) {
		this.restDayApril = restDayApril;
	}

	public String getRestDayMay() {
		return restDayMay;
	}

	public void setRestDayMay(String restDayMay) {
		this.restDayMay = restDayMay;
	}

	public String getRestDayJune() {
		return restDayJune;
	}

	public void setRestDayJune(String restDayJune) {
		this.restDayJune = restDayJune;
	}

	public String getRestDayJuly() {
		return restDayJuly;
	}

	public void setRestDayJuly(String restDayJuly) {
		this.restDayJuly = restDayJuly;
	}

	public String getRestDaySeptember() {
		return restDaySeptember;
	}

	public void setRestDaySeptember(String restDaySeptember) {
		this.restDaySeptember = restDaySeptember;
	}

	public String getRestDayAugust() {
		return restDayAugust;
	}

	public void setRestDayAugust(String restDayAugust) {
		this.restDayAugust = restDayAugust;
	}

	public String getRestDayOctober() {
		return restDayOctober;
	}

	public void setRestDayOctober(String restDayOctober) {
		this.restDayOctober = restDayOctober;
	}

	public String getRestDayNovember() {
		return restDayNovember;
	}

	public void setRestDayNovember(String restDayNovember) {
		this.restDayNovember = restDayNovember;
	}

	public String getRestDayDecember() {
		return restDayDecember;
	}

	public void setRestDayDecember(String restDayDecember) {
		this.restDayDecember = restDayDecember;
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
		return this.cmyFcWorkingCalendarId;
	}

	@Override
	public String toString() {
		return "WorkingCalendarPO{" +
			"cmyFcWorkingCalendarId=" + cmyFcWorkingCalendarId +
			", workingCalendarName=" + workingCalendarName +
			", managementId=" + managementId +
			", managementName=" + managementName +
			", year=" + year +
			", workDayJanuary=" + workDayJanuary +
			", workDayFebruary=" + workDayFebruary +
			", workDayApril=" + workDayApril +
			", workDayMarch=" + workDayMarch +
			", workDayMay=" + workDayMay +
			", workDayJune=" + workDayJune +
			", workDayJuly=" + workDayJuly +
			", workDaySeptember=" + workDaySeptember +
			", workDayAugust=" + workDayAugust +
			", workDayOctober=" + workDayOctober +
			", workDayNovember=" + workDayNovember +
			", workDayDecember=" + workDayDecember +
			", restDayJanuary=" + restDayJanuary +
			", restDayFebruary=" + restDayFebruary +
			", restDayMarch=" + restDayMarch +
			", restDayApril=" + restDayApril +
			", restDayMay=" + restDayMay +
			", restDayJune=" + restDayJune +
			", restDayJuly=" + restDayJuly +
			", restDaySeptember=" + restDaySeptember +
			", restDayAugust=" + restDayAugust +
			", restDayOctober=" + restDayOctober +
			", restDayNovember=" + restDayNovember +
			", restDayDecember=" + restDayDecember +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
