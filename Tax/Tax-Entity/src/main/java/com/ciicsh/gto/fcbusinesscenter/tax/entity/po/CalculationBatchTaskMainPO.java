package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

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
	private Date createdTime;
    /**
     * 修改时间
     */
	private Date modifiedTime;


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
