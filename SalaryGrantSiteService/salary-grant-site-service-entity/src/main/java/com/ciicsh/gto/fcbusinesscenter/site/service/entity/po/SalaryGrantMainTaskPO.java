package com.ciicsh.gto.fcbusinesscenter.site.service.entity.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 薪资发放任务单主表
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-22
 */
@TableName("sg_salary_grant_main_task")
public class SalaryGrantMainTaskPO extends Model<SalaryGrantMainTaskPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务单ID
     */
	@TableId(value="salary_grant_main_task_id", type= IdType.AUTO)
	private Long salaryGrantMainTaskId;
	/**
	 * 任务单编号
	 */
	@TableField("salary_grant_main_task_code")
	private String salaryGrantMainTaskCode;
    /**
     * 流程编号
     */
	@TableField("work_flow_process_id")
	private String workFlowProcessId;
    /**
     * 管理方编号
     */
	@TableField("management_id")
	private String managementId;
    /**
     * 管理方名称
     */
	@TableField("management_name")
	private String managementName;
    /**
     * 薪酬计算批次号
     */
	@TableField("batch_code")
	private String batchCode;
    /**
     * 参考批次号
     */
	@TableField("origin_batch_code")
	private String originBatchCode;
    /**
     * 薪资周期
     */
	@TableField("grant_cycle")
	private String grantCycle;
    /**
     * 薪资发放总金额（RMB）
     */
	@TableField("payment_total_sum")
	private BigDecimal paymentTotalSum;
    /**
     * 发薪人数
     */
	@TableField("total_person_count")
	private Integer totalPersonCount;
    /**
     * 中方发薪人数
     */
	@TableField("chinese_count")
	private Integer chineseCount;
    /**
     * 外方发薪人数
     */
	@TableField("foreigner_count")
	private Integer foreignerCount;
    /**
     * 中智上海发薪人数
     */
	@TableField("local_grant_count")
	private Integer localGrantCount;
    /**
     * 中智代发（委托机构）发薪人数
     */
	@TableField("supplier_grant_count")
	private Integer supplierGrantCount;
    /**
     * 薪资发放日期
     */
	@TableField("grant_date")
	private String grantDate;
    /**
     * 薪资发放时段:1-上午，2-下午
     */
	@TableField("grant_time")
	private Integer grantTime;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
     */
	@TableField("grant_type")
	private Integer grantType;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
	@TableField("grant_mode")
	private String grantMode;
	/**
	 * 是否内转:0-非内转，1-内转
	 */
	@TableField("is_adversion")
	private Boolean isAdversion;
	/**
	 * 内转类型:1-AF转FC,2-BPO转FC,3-FC转AF,4-FC转BPO
	 */
	@TableField("adversion_type")
	private Integer adversionType;
    /**
     * 备注:任务单中雇员信息变化提示链接
     */
	private String remark;
    /**
     * 失效原因
     */
	@TableField("invalid_reason")
	private String invalidReason;
	/**
	 * 审批意见
	 */
	@TableField("approved_opinion")
	private String approvedOpinion;
    /**
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待支付，6-未支付，7-已支付，8-撤回，9-驳回
     */
	@TableField("task_status")
	private String taskStatus;
    /**
     * 任务单类型:0-主表
     */
	@TableField("task_type")
	private Integer taskType;
	/**
	 * 外币发放标识:0-否，1-是
	 */
	@TableField("is_include_foreign_currency")
	private Boolean isIncludeForeignCurrency;
    /**
     * 操作员
     */
    @TableField("operator_user_id")
    private String operatorUserId;
    /**
     * 审核员
     */
    @TableField("approve_user_id")
    private String approveUserId;
    /**
     * 是否有效:1-有效，0-无效
     */
	@TableField("is_active")
	private Boolean isActive;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 最后修改人
     */
	@TableField("modified_by")
	private String modifiedBy;
    /**
     * 最后修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;


	public Long getSalaryGrantMainTaskId() {
		return salaryGrantMainTaskId;
	}

	public void setSalaryGrantMainTaskId(Long salaryGrantMainTaskId) {
		this.salaryGrantMainTaskId = salaryGrantMainTaskId;
	}

	public String getSalaryGrantMainTaskCode() {
		return salaryGrantMainTaskCode;
	}

	public void setSalaryGrantMainTaskCode(String salaryGrantMainTaskCode) {
		this.salaryGrantMainTaskCode = salaryGrantMainTaskCode;
	}

	public String getWorkFlowProcessId() {
		return workFlowProcessId;
	}

	public void setWorkFlowProcessId(String workFlowProcessId) {
		this.workFlowProcessId = workFlowProcessId;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public String getManagementName() {
		return managementName;
	}

	public void setManagementName(String managementName) {
		this.managementName = managementName;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getOriginBatchCode() {
		return originBatchCode;
	}

	public void setOriginBatchCode(String originBatchCode) {
		this.originBatchCode = originBatchCode;
	}

	public String getGrantCycle() {
		return grantCycle;
	}

	public void setGrantCycle(String grantCycle) {
		this.grantCycle = grantCycle;
	}

	public BigDecimal getPaymentTotalSum() {
		return paymentTotalSum;
	}

	public void setPaymentTotalSum(BigDecimal paymentTotalSum) {
		this.paymentTotalSum = paymentTotalSum;
	}

	public Integer getTotalPersonCount() {
		return totalPersonCount;
	}

	public void setTotalPersonCount(Integer totalPersonCount) {
		this.totalPersonCount = totalPersonCount;
	}

	public Integer getChineseCount() {
		return chineseCount;
	}

	public void setChineseCount(Integer chineseCount) {
		this.chineseCount = chineseCount;
	}

	public Integer getForeignerCount() {
		return foreignerCount;
	}

	public void setForeignerCount(Integer foreignerCount) {
		this.foreignerCount = foreignerCount;
	}

	public Integer getLocalGrantCount() {
		return localGrantCount;
	}

	public void setLocalGrantCount(Integer localGrantCount) {
		this.localGrantCount = localGrantCount;
	}

	public Integer getSupplierGrantCount() {
		return supplierGrantCount;
	}

	public void setSupplierGrantCount(Integer supplierGrantCount) {
		this.supplierGrantCount = supplierGrantCount;
	}

	public String getGrantDate() {
		return grantDate;
	}

	public void setGrantDate(String grantDate) {
		this.grantDate = grantDate;
	}

	public Integer getGrantTime() {
		return grantTime;
	}

	public void setGrantTime(Integer grantTime) {
		this.grantTime = grantTime;
	}

	public Integer getGrantType() {
		return grantType;
	}

	public void setGrantType(Integer grantType) {
		this.grantType = grantType;
	}

	public String getGrantMode() {
		return grantMode;
	}

	public void setGrantMode(String grantMode) {
		this.grantMode = grantMode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Boolean getAdversion() {
		return isAdversion;
	}

	public void setAdversion(Boolean adversion) {
		isAdversion = adversion;
	}

	public Integer getAdversionType() {
		return adversionType;
	}

	public void setAdversionType(Integer adversionType) {
		this.adversionType = adversionType;
	}

	public String getApprovedOpinion() {
		return approvedOpinion;
	}

	public void setApprovedOpinion(String approvedOpinion) {
		this.approvedOpinion = approvedOpinion;
	}

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(String approveUserId) {
        this.approveUserId = approveUserId;
    }

	public Boolean getIncludeForeignCurrency() {
		return isIncludeForeignCurrency;
	}

	public void setIncludeForeignCurrency(Boolean includeForeignCurrency) {
		isIncludeForeignCurrency = includeForeignCurrency;
	}

	@Override
	protected Serializable pkVal() {
		return this.salaryGrantMainTaskId;
	}

	@Override
	public String toString() {
		return "SalaryGrantMainTask{" +
			", salaryGrantMainTaskCode=" + salaryGrantMainTaskCode +
			", salaryGrantMainTaskId=" + salaryGrantMainTaskId +
			", workFlowProcessId=" + workFlowProcessId +
			", managementId=" + managementId +
			", managementName=" + managementName +
			", batchCode=" + batchCode +
			", originBatchCode=" + originBatchCode +
			", grantCycle=" + grantCycle +
			", paymentTotalSum=" + paymentTotalSum +
			", totalPersonCount=" + totalPersonCount +
			", chineseCount=" + chineseCount +
			", foreignerCount=" + foreignerCount +
			", localGrantCount=" + localGrantCount +
			", supplierGrantCount=" + supplierGrantCount +
			", grantDate=" + grantDate +
			", grantTime=" + grantTime +
			", grantType=" + grantType +
			", grantMode=" + grantMode +
			", isAdversion=" + isAdversion +
			", adversionType=" + adversionType +
			", remark=" + remark +
			", invalidReason=" + invalidReason +
			", approvedOpinion=" + approvedOpinion +
			", taskStatus=" + taskStatus +
			", taskType=" + taskType +
			", isIncludeForeignCurrency=" + isIncludeForeignCurrency +
            ", operatorUserId=" + operatorUserId +
            ", approveUserId=" + approveUserId +
			", isActive=" + isActive +
			", createdBy=" + createdBy +
			", createdTime=" + createdTime +
			", modifiedBy=" + modifiedBy +
			", modifiedTime=" + modifiedTime +
			"}";
	}
}
