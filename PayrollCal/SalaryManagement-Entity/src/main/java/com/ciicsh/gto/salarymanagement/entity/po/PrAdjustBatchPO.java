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
 * 调整批次主表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_adjust_batch")
public class PrAdjustBatchPO extends Model<PrAdjustBatchPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 参考批次，即被调整的正常批次
     */
	@TableField("root_batch_code")
	private String rootBatchCode;

	/**
	 * 参考批次，正常批次CODE
	 */
	@TableField("origin_batch_code")
	private String originBatchCode;
    /**
     * 调整批次编号： A-客户ID-计算日期-4位序号
     */
	@TableField("adjust_batch_code")
	private String adjustBatchCode;

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
     * 备注
     */
	private String remark;
    /**
     * 批次状态：
1-新建
2-计算中
3-计算完成
4-审核中
5-审核完成
6-关账
7-已发放
8-个税已申报
     */
	private Integer status;
    /**
     * 调整后薪资计算结果
     */
	@TableField("adjust_result")
	private String adjustResult;
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
	private Boolean hasAdvance;

	/*是否来款：0表示未来款，1表示已来款*/
	@TableField("has_money")
	private Boolean hasMoney;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRootBatchCode() {
		return rootBatchCode;
	}

	public void setRootBatchCode(String rootBatchCode) {
		this.rootBatchCode = rootBatchCode;
	}

	public String getOriginBatchCode() {
		return originBatchCode;
	}

	public void setOriginBatchCode(String originBatchCode) {
		this.originBatchCode = originBatchCode;
	}

	public String getAdjustBatchCode() {
		return adjustBatchCode;
	}

	public void setAdjustBatchCode(String adjustBatchCode) {
		this.adjustBatchCode = adjustBatchCode;
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

	public String getAdjustResult() {
		return adjustResult;
	}

	public void setAdjustResult(String adjustResult) {
		this.adjustResult = adjustResult;
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

	public Boolean getHasAdvance() {
		return hasAdvance;
	}

	public void setHasAdvance(Boolean hasAdvance) {
		this.hasAdvance = hasAdvance;
	}

	public Boolean getHasMoney() {
		return hasMoney;
	}

	public void setHasMoney(Boolean hasMoney) {
		this.hasMoney = hasMoney;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PrAdjustBatchPO{" +
			"id=" + id +
			", originBatchCode=" + originBatchCode +
			", adjustBatchCode=" + adjustBatchCode +
			", isScheduled=" + isScheduled +
			", scheduleSetting=" + scheduleSetting +
			", remark=" + remark +
			", status=" + status +
			", adjustResult=" + adjustResult +
			", isActive=" + isActive +
			", hasMoney=" + hasMoney +
			", hasAdvance=" + hasAdvance +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
