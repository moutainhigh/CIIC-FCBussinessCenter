package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 划款子任务
 * </p>
 *
 * @author yuantongqing
 * @since 2018-01-08
 */
@TableName("tax_fc_task_sub_money")
public class TaskSubMoneyPO{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 主任务ID（为空，则为合并任务，被合并的子任务可能来自不同的主任务）
     */
	@TableField("task_main_id")
	private Long taskMainId;
    /**
     * 划款子任务ID（如果非空，则任务已合并，记录合并后的子任务ID）
     */
	@TableField("task_sub_money_id")
	private Long taskSubMoneyId;
    /**
     * 任务编号
     */
	@TableField("task_no")
	private String taskNo;
    /**
     * 缴纳账户
     */
	@TableField("payment_account")
	private String paymentAccount;
    /**
     * 个税期间
     */
	private LocalDate period;
    /**
     * 个税总金额
     */
	@TableField("tax_amount")
	private BigDecimal taxAmount;
    /**
     * 总人数
     */
	private Integer headcount;
    /**
     * 中方人数
     */
	@TableField("chinese_num")
	private Integer chineseNum;
    /**
     * 外方人数
     */
	@TableField("foreigner_num")
	private Integer foreignerNum;
    /**
     * 状态
     */
	private String status;
	/**
	 * 状态中文
	 */
	@TableField(exist = false)
	private String statusName;
    /**
     * 是否可用
     */
	@TableField("is_active")
	private Boolean isActive;
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
     * 创建人
     */
    @TableField(value="created_by",fill = FieldFill.INSERT)
	private String createdBy;
    /**
     * 修改人
     */
    @TableField(value="modified_by",fill = FieldFill.INSERT_UPDATE)
	private String modifiedBy;
    /**
     * 管理方编号
     */
	@TableField("manager_no")
	private String managerNo;
    /**
     * 管理方名称
     */
	@TableField("manager_name")
	private String managerName;
	/**
	 * 划款状态
	 */
	@TableField("pay_status")
	private String payStatus;
	/**
	 * 划款状态(中文)
	 */
	@TableField(exist = false)
	private String payStatusName;

	/**
	 * 付款申请ID
	 */
	@TableField("pay_apply_id")
	private Long payApplyId;

	/**
	 * 付款凭证编号(结算单编号)
	 */
	@TableField("pay_apply_code")
	private String payApplyCode;

	/**
	 * 区域类型
	 */
	private String areaType;

	/**
	 * 创建人displayname
	 */
	@TableField(value="created_by_display_name",fill = FieldFill.INSERT)
	private String createdByDisplayName;
	/**
	 * 修改人displayname
	 */
	@TableField(value="modified_by_display_name",fill = FieldFill.INSERT_UPDATE)
	private String modifiedByDisplayName;

	/**
	 * 滞纳金
	 */
	private BigDecimal overdue;
	/**
	 * 罚金
	 */
	private BigDecimal fine;

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

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskMainId() {
		return taskMainId;
	}

	public void setTaskMainId(Long taskMainId) {
		this.taskMainId = taskMainId;
	}

	public Long getTaskSubMoneyId() {
		return taskSubMoneyId;
	}

	public void setTaskSubMoneyId(Long taskSubMoneyId) {
		this.taskSubMoneyId = taskSubMoneyId;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
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

		if(status!=null){

			this.statusName  = EnumUtil.getMessage(EnumUtil.TASK_STATUS,status);
		}
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDate getPeriod() {
		return period;
	}

	public void setPeriod(LocalDate period) {
		this.period = period;
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
		if(payStatus!=null){

			this.payStatusName  = EnumUtil.getMessage(EnumUtil.PAY_STATUS,payStatus);
		}
	}

	public String getPayStatusName() {
		return payStatusName;
	}

	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}

	public Long getPayApplyId() {
		return payApplyId;
	}

	public void setPayApplyId(Long payApplyId) {
		this.payApplyId = payApplyId;
	}

	public String getPayApplyCode() {
		return payApplyCode;
	}

	public void setPayApplyCode(String payApplyCode) {
		this.payApplyCode = payApplyCode;
	}

	public BigDecimal getOverdue() {
		return overdue;
	}

	public void setOverdue(BigDecimal overdue) {
		this.overdue = overdue;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}

	@Override
	public String toString() {
		return "TaskSubMoneyPO{" +
				"id=" + id +
				", taskMainId=" + taskMainId +
				", taskSubMoneyId=" + taskSubMoneyId +
				", taskNo='" + taskNo + '\'' +
				", paymentAccount='" + paymentAccount + '\'' +
				", period=" + period +
				", taxAmount=" + taxAmount +
				", headcount=" + headcount +
				", chineseNum=" + chineseNum +
				", foreignerNum=" + foreignerNum +
				", status='" + status + '\'' +
				", statusName='" + statusName + '\'' +
				", isActive=" + isActive +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", createdBy='" + createdBy + '\'' +
				", modifiedBy='" + modifiedBy + '\'' +
				", managerNo='" + managerNo + '\'' +
				", managerName='" + managerName + '\'' +
				", payStatus='" + payStatus + '\'' +
				", payStatusName='" + payStatusName + '\'' +
				", payApplyId=" + payApplyId +
				", payApplyCode='" + payApplyCode + '\'' +
				", areaType='" + areaType + '\'' +
				", createdByDisplayName='" + createdByDisplayName + '\'' +
				", modifiedByDisplayName='" + modifiedByDisplayName + '\'' +
				", overdue=" + overdue +
				", fine=" + fine +
				'}';
	}
}
