package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 薪资发放任务单子表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@TableName("sg_salary_grant_sub_task")
public class SalaryGrantSubTaskPO extends Model<SalaryGrantSubTaskPO> implements Cloneable{

    private static final long serialVersionUID = 1L;

    /**
     * 任务单ID
     */
	@TableId(value="salary_grant_sub_task_id", type= IdType.AUTO)
	private Long salaryGrantSubTaskId;
    /**
     * 任务单编号
     */
	@TableField("salary_grant_sub_task_code")
	private String salaryGrantSubTaskCode;
    /**
     * 任务单主表编号
     */
	@TableField("salary_grant_main_task_code")
	private String salaryGrantMainTaskCode;
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
     * 发放账户编号
     */
	@TableField("grant_account_code")
	private String grantAccountCode;
	/**
	 * 发放账户名称
	 */
	@TableField("grant_account_name")
	private String grantAccountName;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
     */
    @TableField("grant_type")
    private Integer grantType;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
     */
	@TableField("grant_mode")
	private Integer grantMode;
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
	 * 是否垫付:0-否，1-是
	 */
	@TableField("is_advance")
	private Boolean isAdvance;
	/**
	 * 垫付类型:1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付
	 */
	@TableField("advance_type")
	private Integer advanceType;
	/**
	 * 是否处理:1-已处理，0-未处理
	 */
	@TableField("is_process")
	private Boolean isProcess;
    /**
     * 备注:任务单中雇员信息变化提示链接
     */
	private String remark;
    /**
     * 审批意见
     */
    @TableField("approved_opinion")
    private String approvedOpinion;
    /**
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待生成，6-未支付，7-已支付，8-撤回，9-驳回，10-待合并，11-已合并，12-已确认，13-作废
     */
	@TableField("task_status")
	private String taskStatus;
    /**
     * 任务单类型:1-中智上海账户、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
     */
	@TableField("task_type")
	private Integer taskType;
	/**
	 * 结算发放标识:0-正常，1-垫付
	 */
	@TableField("balance_grant")
	private Integer balanceGrant;
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
	 * 任务流转信息
	 */
	@TableField("work_flow_user_info")
	private String workFlowUserInfo;
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

	public Long getSalaryGrantSubTaskId() {
		return salaryGrantSubTaskId;
	}

	public void setSalaryGrantSubTaskId(Long salaryGrantSubTaskId) {
		this.salaryGrantSubTaskId = salaryGrantSubTaskId;
	}

	public String getSalaryGrantSubTaskCode() {
		return salaryGrantSubTaskCode;
	}

	public void setSalaryGrantSubTaskCode(String salaryGrantSubTaskCode) {
		this.salaryGrantSubTaskCode = salaryGrantSubTaskCode;
	}

	public String getSalaryGrantMainTaskCode() {
		return salaryGrantMainTaskCode;
	}

	public void setSalaryGrantMainTaskCode(String salaryGrantMainTaskCode) {
		this.salaryGrantMainTaskCode = salaryGrantMainTaskCode;
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

	public String getGrantCycle() {
		return grantCycle;
	}

	public void setGrantCycle(String grantCycle) {
		this.grantCycle = grantCycle;
	}

	public String getGrantAccountCode() {
		return grantAccountCode;
	}

	public void setGrantAccountCode(String grantAccountCode) {
		this.grantAccountCode = grantAccountCode;
	}

	public Integer getGrantMode() {
		return grantMode;
	}

	public void setGrantMode(Integer grantMode) {
		this.grantMode = grantMode;
	}

	public Integer getGrantType() {
		return grantType;
	}

	public void setGrantType(Integer grantType) {
		this.grantType = grantType;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getGrantAccountName() {
		return grantAccountName;
	}

	public void setGrantAccountName(String grantAccountName) {
		this.grantAccountName = grantAccountName;
	}

	public Boolean getProcess() {
		return isProcess;
	}

	public void setProcess(Boolean process) {
		isProcess = process;
	}

	public String getWorkFlowUserInfo() {
		return workFlowUserInfo;
	}

	public void setWorkFlowUserInfo(String workFlowUserInfo) {
		this.workFlowUserInfo = workFlowUserInfo;
	}

	public Boolean getAdvance() {
		return isAdvance;
	}

	public void setAdvance(Boolean advance) {
		isAdvance = advance;
	}

	public Integer getAdvanceType() {
		return advanceType;
	}

	public void setAdvanceType(Integer advanceType) {
		this.advanceType = advanceType;
	}

	public Integer getBalanceGrant() {
		return balanceGrant;
	}

	public void setBalanceGrant(Integer balanceGrant) {
		this.balanceGrant = balanceGrant;
	}

	@Override
	protected Serializable pkVal() {
		return this.salaryGrantSubTaskId;
	}

	// 重写克隆方法子列才可以调用
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "SalaryGrantSubTaskPO{" +
			", salaryGrantSubTaskId=" + salaryGrantSubTaskId +
			", salaryGrantSubTaskCode=" + salaryGrantSubTaskCode +
			", salaryGrantMainTaskCode=" + salaryGrantMainTaskCode +
			", managementId=" + managementId +
			", managementName=" + managementName +
			", batchCode=" + batchCode +
			", grantCycle=" + grantCycle +
            ", paymentTotalSum=" + paymentTotalSum +
            ", totalPersonCount=" + totalPersonCount +
            ", chineseCount=" + chineseCount +
            ", foreignerCount=" + foreignerCount +
            ", grantDate=" + grantDate +
            ", grantTime=" + grantTime +
			", grantAccountCode=" + grantAccountCode +
			", grantAccountName=" + grantAccountName +
            ", grantType=" + grantType +
			", grantMode=" + grantMode +
            ", isAdversion=" + isAdversion +
            ", adversionType=" + adversionType +
			", isAdvance=" + isAdvance +
			", advanceType=" + advanceType +
			", isProcess=" + isProcess +
			", remark=" + remark +
            ", approvedOpinion=" + approvedOpinion +
            ", taskStatus=" + taskStatus +
			", taskType=" + taskType +
			", balanceGrant=" + balanceGrant +
            ", operatorUserId=" + operatorUserId +
            ", approveUserId=" + approveUserId +
			", workFlowUserInfo=" + workFlowUserInfo +
			", isActive=" + isActive +
			", createdBy=" + createdBy +
			", createdTime=" + createdTime +
			", modifiedBy=" + modifiedBy +
			", modifiedTime=" + modifiedTime +
			"}";
	}
}
