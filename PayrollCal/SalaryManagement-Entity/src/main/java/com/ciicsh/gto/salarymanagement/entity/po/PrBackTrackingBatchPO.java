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
 * 回溯批次主表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_back_tracking_batch")
public class PrBackTrackingBatchPO extends Model<PrBackTrackingBatchPO> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 参考批次，即被回溯的批次ID
     */
	@TableField("origin_batch_code")
	private String originBatchCode;
    /**
     * 回溯批次编号： B-客户ID-计算日期-4位序号
     */
	@TableField("back_tracking_batch_code")
	private String backTrackingBatchCode;
    /**
     * 所属管理方ID
     */
	@TableField("management_id")
	private String managementId;
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
     * 回溯调整雇员薪资字段JSON格式（用于记录回溯调整计算前雇员的变更字段值）
     */
	@TableField("back_emp_adjust_fields")
	private String backEmpAdjustFields;
    /**
     * 回溯调整雇员特殊计算项字段：雇员ID，雇员个税合计，雇员实际发工资
     */
	@TableField("back_emp_spec_fields")
	private String backEmpSpecFields;
    /**
     * 回溯调整计算结果
     */
	@TableField("back_emp_result")
	private String backEmpResult;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriginBatchCode() {
		return originBatchCode;
	}

	public void setOriginBatchCode(String originBatchCode) {
		this.originBatchCode = originBatchCode;
	}

	public String getBackTrackingBatchCode() {
		return backTrackingBatchCode;
	}

	public void setBackTrackingBatchCode(String backTrackingBatchCode) {
		this.backTrackingBatchCode = backTrackingBatchCode;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
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

	public String getBackEmpAdjustFields() {
		return backEmpAdjustFields;
	}

	public void setBackEmpAdjustFields(String backEmpAdjustFields) {
		this.backEmpAdjustFields = backEmpAdjustFields;
	}

	public String getBackEmpSpecFields() {
		return backEmpSpecFields;
	}

	public void setBackEmpSpecFields(String backEmpSpecFields) {
		this.backEmpSpecFields = backEmpSpecFields;
	}

	public String getBackEmpResult() {
		return backEmpResult;
	}

	public void setBackEmpResult(String backEmpResult) {
		this.backEmpResult = backEmpResult;
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

	@Override
	public String toString() {
		return "PrBackTrackingBatchPO{" +
			"id=" + id +
			", originBatchCode=" + originBatchCode +
			", backTrackingBatchCode=" + backTrackingBatchCode +
			", managementId=" + managementId +
			", remark=" + remark +
			", status=" + status +
			", backEmpAdjustFields=" + backEmpAdjustFields +
			", backEmpSpecFields=" + backEmpSpecFields +
			", backEmpResult=" + backEmpResult +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
