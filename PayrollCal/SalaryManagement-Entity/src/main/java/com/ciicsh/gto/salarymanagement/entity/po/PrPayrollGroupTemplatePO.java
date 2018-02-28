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
 * 配置表，薪资组模板表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_payroll_group_template")
public class PrPayrollGroupTemplatePO extends Model<PrPayrollGroupTemplatePO> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增薪资组ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 薪资组模板编码，规则为：XZZMB-xxxxxx(六位数字序号)
     */
	@TableField("group_template_code")
	private String groupTemplateCode;
    /**
     * 薪资组模板名称
     */
	@TableField("group_template_name")
	private String groupTemplateName;
    /**
     * 版本号
     */
	private String version;
    /**
     * 审核状态：
1 审核中；
2 审核完成；
3 拒绝；
     */
	@TableField("approval_status")
	private Integer approvalStatus;
    /**
     * 审核意见
     */
	private String comments;
    /**
     * 备注
     */
	private String remark;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupTemplateCode() {
		return groupTemplateCode;
	}

	public void setGroupTemplateCode(String groupTemplateCode) {
		this.groupTemplateCode = groupTemplateCode;
	}

	public String getGroupTemplateName() {
		return groupTemplateName;
	}

	public void setGroupTemplateName(String groupTemplateName) {
		this.groupTemplateName = groupTemplateName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PrPayrollGroupTemplatePO{" +
				"id=" + id +
				", groupTemplateCode='" + groupTemplateCode + '\'' +
				", groupTemplateName='" + groupTemplateName + '\'' +
				", version='" + version + '\'' +
				", approvalStatus=" + approvalStatus +
				", comments='" + comments + '\'' +
				", remark='" + remark + '\'' +
				", isActive=" + isActive +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", createdBy='" + createdBy + '\'' +
				", modifiedBy='" + modifiedBy + '\'' +
				'}';
	}
}
