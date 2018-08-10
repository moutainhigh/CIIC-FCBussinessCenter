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
 * 配置表，薪资组表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_payroll_group")
public class PrPayrollGroupPO extends Model<PrPayrollGroupPO> {

    private static final long serialVersionUID = 8044200329507925665L;
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 薪资组编码，规则为：XZZ-客户ID-xxx(三位数字序号)
     */
    @TableField("group_code")
    private String groupCode;
    /**
     * 所属管理方ID
     */
    @TableField("management_id")
    private String managementId;
    /**
     * 继承薪资组模板ID
     */
    @TableField("group_template_code")
    private String groupTemplateCode;
    /**
     * 薪资组名称
     */
    @TableField("group_name")
    private String groupName;
    /**
     * 版本号
     */
    private String version;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否是薪资组模板 - 管理方和客户都是财务公司（FC）时默认为是
     */
    @TableField("is_template")
    private Boolean isTemplate;
    /**
     * 审核状态：
     * 1 审核中；
     * 2 审核通过；
     * 3 拒绝；
     */
    @TableField("approval_status")
    private Integer approvalStatus;
    /**
     * 审核意见
     */
    private String comments;
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
    /**
     * 雇员扩展字段模板ID
     */
    @TableField("emp_extend_field_template_id")
    private Long empExtendFieldTemplateId;
    /**
     * 雇员扩展字段模板名称
     */
    @TableField("emp_extend_field_template_name")
    private String empExtendFieldTemplateName;

    // 操作类型 1:添加; 2:更新; 3:删除; 4:查询; 5:导入; 6:复制;
    @TableField(exist = false)
    private int operateType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public String getGroupTemplateCode() {
        return groupTemplateCode;
    }

    public void setGroupTemplateCode(String groupTemplateCode) {
        this.groupTemplateCode = groupTemplateCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
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

    public Long getEmpExtendFieldTemplateId() {
        return empExtendFieldTemplateId;
    }

    public void setEmpExtendFieldTemplateId(Long empExtendFieldTemplateId) {
        this.empExtendFieldTemplateId = empExtendFieldTemplateId;
    }

    public String getEmpExtendFieldTemplateName() {
        return empExtendFieldTemplateName;
    }

    public void setEmpExtendFieldTemplateName(String empExtendFieldTemplateName) {
        this.empExtendFieldTemplateName = empExtendFieldTemplateName;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PrPayrollGroupPO{" +
                "id=" + id +
                ", groupCode='" + groupCode + '\'' +
                ", managementId='" + managementId + '\'' +
                ", groupTemplateCode='" + groupTemplateCode + '\'' +
                ", groupName='" + groupName + '\'' +
                ", version='" + version + '\'' +
                ", remark='" + remark + '\'' +
                ", isTemplate=" + isTemplate +
                ", approvalStatus=" + approvalStatus +
                ", comments='" + comments + '\'' +
                ", isActive=" + isActive +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", empExtendFieldTemplateId=" + empExtendFieldTemplateId +
                ", empExtendFieldTemplateName=" + empExtendFieldTemplateName +
                "} " + super.toString();
    }
}
