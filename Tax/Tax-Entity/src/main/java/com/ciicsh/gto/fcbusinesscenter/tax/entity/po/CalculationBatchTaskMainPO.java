package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 计算批次和主任务关联表
 * </p>
 *
 * @author wuhua
 * @since 2018-01-23
 */
@TableName("tax_fc_calculation_batch_task_main")
public class CalculationBatchTaskMainPO extends Model<CalculationBatchTaskMainPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 计算批次ID
     */
	private Long calBatchId;
    /**
     * 引擎计算批次号
     */
	private String batchNo;
    /**
     * 主任务ID
     */
	private Long taskMainId;
    /**
     * 创建时间
     */
    @TableField(value="created_time",fill = FieldFill.INSERT)
	private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    @TableField(value="modified_time",fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime modifiedTime;

	/**
	 *  主任务
	 */
	@TableField(exist = false)
	private TaskMainPO taskMainPOs;

	private int versionNo;

	public int getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(int versionNo) {
		this.versionNo = versionNo;
	}

	public TaskMainPO getTaskMainPOs() {
		return taskMainPOs;
	}

	public void setTaskMainPOs(TaskMainPO taskMainPOs) {
		this.taskMainPOs = taskMainPOs;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCalBatchId() {
		return calBatchId;
	}

	public void setCalBatchId(Long calBatchId) {
		this.calBatchId = calBatchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Long getTaskMainId() {
		return taskMainId;
	}

	public void setTaskMainId(Long taskMainId) {
		this.taskMainId = taskMainId;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CalculationBatchTaskMain{" +
			"id=" + id +
			", calBatchId=" + calBatchId +
			", batchNo=" + batchNo +
			", taskMainId=" + taskMainId +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			"}";
	}
}
