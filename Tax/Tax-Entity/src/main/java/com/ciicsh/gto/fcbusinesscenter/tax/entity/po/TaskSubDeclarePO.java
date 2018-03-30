package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 申报子任务
 * </p>
 *
 * @author wuhua
 * @since 2018-01-24
 */
@TableName("tax_fc_task_sub_declare")
public class TaskSubDeclarePO extends Model<TaskSubDeclarePO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 主任务ID（为空，则为合并任务，被合并的申报子任务可能来自不同的主任务）
     */
	private Long taskMainId;
    /**
     * 申报子任务ID（如果非空，则任务已合并，记录合并后的申报子任务ID）
     */
	@TableField(value="task_sub_declare_id",fill= FieldFill.UPDATE)
	private Long taskSubDeclareId;
    /**
     * 任务编号
     */
	private String taskNo;
    /**
     * 申报账户
     */
	private String declareAccount;
    /**
     * 个税期间
     */
	private LocalDate period;
    /**
     * 滞纳金
     */
	private BigDecimal overdue;
    /**
     * 罚金
     */
	private BigDecimal fine;
    /**
     * 个税总金额
     */
	private BigDecimal taxAmount;
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
    @TableLogic
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
	private String managerNo;
    /**
     * 管理方名称
     */
	private String managerName;

	/**
	 * 是否为合并任务
	 */
	@TableField("is_combined")
	private Boolean isCombined;

	/**
	 * 是否有合并明细
	 */
	@TableField("has_combined")
	private Boolean hasCombined;

	/**
	 * 账户类型
	 */
	private String accountType;

	/**
	 * 区域类型
	 */
	private String areaType;

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Boolean getHasCombined() {
		return hasCombined;
	}

	public void setHasCombined(Boolean hasCombined) {
		this.hasCombined = hasCombined;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

	public Long getTaskSubDeclareId() {
		return taskSubDeclareId;
	}

	public void setTaskSubDeclareId(Long taskSubDeclareId) {
		this.taskSubDeclareId = taskSubDeclareId;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getDeclareAccount() {
		return declareAccount;
	}

	public void setDeclareAccount(String declareAccount) {
		this.declareAccount = declareAccount;
	}

	public LocalDate getPeriod() {
		return period;
	}

	public void setPeriod(LocalDate period) {
		this.period = period;
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

	public Boolean getCombined() {
		return isCombined;
	}

	public void setCombined(Boolean combined) {
		isCombined = combined;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TaskSubDeclarePO{" +
				"id=" + id +
				", taskMainId=" + taskMainId +
				", taskSubDeclareId=" + taskSubDeclareId +
				", taskNo='" + taskNo + '\'' +
				", declareAccount='" + declareAccount + '\'' +
				", period=" + period +
				", overdue=" + overdue +
				", fine=" + fine +
				", taxAmount=" + taxAmount +
				", headcount=" + headcount +
				", chineseNum=" + chineseNum +
				", foreignerNum=" + foreignerNum +
				", status='" + status + '\'' +
				", isActive=" + isActive +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", createdBy='" + createdBy + '\'' +
				", modifiedBy='" + modifiedBy + '\'' +
				", managerNo='" + managerNo + '\'' +
				", managerName='" + managerName + '\'' +
				", isCombined=" + isCombined +
				'}';
	}
}
