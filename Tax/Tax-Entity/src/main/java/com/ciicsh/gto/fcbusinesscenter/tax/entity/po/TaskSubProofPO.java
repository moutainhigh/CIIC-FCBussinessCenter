package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 完税凭证子任务
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-07
 */
@TableName("tax_fc_task_sub_proof")
public class TaskSubProofPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 凭证主任务ID
     */
	@TableField("task_main_proof_id")
	private Long taskMainProofId;
    /**
     * 凭证子任务ID（非空，则记录合并后的任务ID）
     */
	@TableField("task_sub_proof_id")
	private Long taskSubProofId;
    /**
     * 任务编号
     */
	@TableField("task_no")
	private String taskNo;
    /**
     * 申报账户
     */
	@TableField("declare_account")
	private String declareAccount;
    /**
     * 个税期间
     */
	private LocalDate period;
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
     * 是否可用
     */
	@TableField("is_active")
	private Boolean isActive;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 修改人
     */
	@TableField("modified_by")
	private String modifiedBy;

	/**
	 * 任务类型(01:自动,02:人工)
	 */
	@TableField("task_type")
	private String taskType;

	/**
	 * 是否为合并任务(
	 */
	@TableField("is_combined")
	private Boolean isCombined;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskMainProofId() {
		return taskMainProofId;
	}

	public void setTaskMainProofId(Long taskMainProofId) {
		this.taskMainProofId = taskMainProofId;
	}

	public Long getTaskSubProofId() {
		return taskSubProofId;
	}

	public void setTaskSubProofId(Long taskSubProofId) {
		this.taskSubProofId = taskSubProofId;
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

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Boolean getCombined() {
		return isCombined;
	}

	public void setCombined(Boolean combined) {
		isCombined = combined;
	}

	@Override
	public String toString() {
		return "TaskSubProofPO{" +
				"id=" + id +
				", taskMainProofId=" + taskMainProofId +
				", taskSubProofId=" + taskSubProofId +
				", taskNo='" + taskNo + '\'' +
				", declareAccount='" + declareAccount + '\'' +
				", period=" + period +
				", headcount=" + headcount +
				", chineseNum=" + chineseNum +
				", foreignerNum=" + foreignerNum +
				", status='" + status + '\'' +
				", isActive=" + isActive +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", createdBy='" + createdBy + '\'' +
				", modifiedBy='" + modifiedBy + '\'' +
				", taskType='" + taskType + '\'' +
				", isCombined='" + isCombined + '\'' +
				'}';
	}
}