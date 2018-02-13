package com.ciicsh.gto.salarymanagement.entity.po;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 审批历史（薪资模版，薪资组，正常批次，调整批次，回溯批次）
 * </p>
 *
 * @author Neo Jiang
 * @since 2018-02-01
 */
@TableName("pr_approval_history")
public class ApprovalHistoryPO extends Model<ApprovalHistoryPO> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 业务编码
     */
	@TableField("biz_code")
	private String bizCode;
    /**
     * 业务类型，枚举：
1 表示薪资组模版；
2 表示薪资组；
3 表示正常批次；
4 表示调整批次；
5 表示回溯批次；
     */
	@TableField("biz_type")
	private Integer bizType;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 1 表示 审核通过
2 表示 审核拒绝
3 表示 待审核
     */
	@TableField("approval_result")
	private Integer approvalResult;
    /**
     * 审核备注
     */
	private String comments;
    /**
     * 创建人姓名（审核人姓名）
     */
	@TableField("created_name")
	private String createdName;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getApprovalResult() {
		return approvalResult;
	}

	public void setApprovalResult(Integer approvalResult) {
		this.approvalResult = approvalResult;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatedName() {
		return createdName;
	}

	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PrApprovalHistory{" +
			"id=" + id +
			", bizCode=" + bizCode +
			", bizType=" + bizType +
			", createdTime=" + createdTime +
			", createdBy=" + createdBy +
			", approvalResult=" + approvalResult +
			", comments=" + comments +
			", createdName=" + createdName +
			"}";
	}
}
