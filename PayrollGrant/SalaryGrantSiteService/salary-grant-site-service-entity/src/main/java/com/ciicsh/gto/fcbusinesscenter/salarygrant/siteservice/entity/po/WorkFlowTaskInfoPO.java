package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工作流任务日志表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-09
 */
@TableName("sg_work_flow_task_info")
public class WorkFlowTaskInfoPO extends Model<WorkFlowTaskInfoPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务日志编号
     */
    @TableId(value="work_flow_task_info_id", type= IdType.AUTO)
    private Long workFlowTaskInfoId;
    /**
     * 流程编号
     */
    @TableField("work_flow_process_id")
    private String workFlowProcessId;
    /**
     * 流程定义key
     */
    @TableField("process_definition_key")
    private String processDefinitionKey;
    /**
     * 任务单编号
     */
    @TableField("task_code")
    private String taskCode;
    /**
     * 任务编号
     */
    @TableField("work_flow_task_id")
    private String workFlowTaskId;
    /**
     * 任务类型
     */
    @TableField("work_flow_task_type")
    private String workFlowTaskType;
    /**
     * 角色id,多个角色以逗号分隔
     */
    @TableField("role_id")
    private String roleId;
    /**
     * 任务审核状态:1-已处理，0-未处理
     */
    @TableField("work_flow_task_status")
    private Boolean workFlowTaskStatus;
    /**
     * 处理人编号
     */
    @TableField("task_deal_user_id")
    private String taskDealUserId;
    /**
     * 处理人名称
     */
    @TableField("task_deal_user_name")
    private String taskDealUserName;
    /**
     * 处理时间
     */
    @TableField("task_deal_time")
    private Date taskDealTime;
    /**
     * 处理操作
     */
    @TableField("task_deal_operation")
    private String taskDealOperation;
    /**
     * 审批意见
     */
    @TableField("approved_opinion")
    private String approvedOpinion;
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


    public Long getWorkFlowTaskInfoId() {
        return workFlowTaskInfoId;
    }

    public void setWorkFlowTaskInfoId(Long workFlowTaskInfoId) {
        this.workFlowTaskInfoId = workFlowTaskInfoId;
    }

    public String getWorkFlowProcessId() {
        return workFlowProcessId;
    }

    public void setWorkFlowProcessId(String workFlowProcessId) {
        this.workFlowProcessId = workFlowProcessId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getWorkFlowTaskId() {
        return workFlowTaskId;
    }

    public void setWorkFlowTaskId(String workFlowTaskId) {
        this.workFlowTaskId = workFlowTaskId;
    }

    public String getWorkFlowTaskType() {
        return workFlowTaskType;
    }

    public void setWorkFlowTaskType(String workFlowTaskType) {
        this.workFlowTaskType = workFlowTaskType;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Boolean getWorkFlowTaskStatus() {
        return workFlowTaskStatus;
    }

    public void setWorkFlowTaskStatus(Boolean workFlowTaskStatus) {
        this.workFlowTaskStatus = workFlowTaskStatus;
    }

    public String getTaskDealUserId() {
        return taskDealUserId;
    }

    public void setTaskDealUserId(String taskDealUserId) {
        this.taskDealUserId = taskDealUserId;
    }

    public String getTaskDealUserName() {
        return taskDealUserName;
    }

    public void setTaskDealUserName(String taskDealUserName) {
        this.taskDealUserName = taskDealUserName;
    }

    public Date getTaskDealTime() {
        return taskDealTime;
    }

    public void setTaskDealTime(Date taskDealTime) {
        this.taskDealTime = taskDealTime;
    }

    public String getTaskDealOperation() {
        return taskDealOperation;
    }

    public void setTaskDealOperation(String taskDealOperation) {
        this.taskDealOperation = taskDealOperation;
    }

    public String getApprovedOpinion() {
        return approvedOpinion;
    }

    public void setApprovedOpinion(String approvedOpinion) {
        this.approvedOpinion = approvedOpinion;
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

    @Override
    protected Serializable pkVal() {
        return this.workFlowTaskInfoId;
    }

    @Override
    public String toString() {
        return "WorkFlowTaskInfoPO{" +
                ", workFlowTaskInfoId=" + workFlowTaskInfoId +
                ", workFlowProcessId=" + workFlowProcessId +
                ", processDefinitionKey=" + processDefinitionKey +
                ", taskCode=" + taskCode +
                ", workFlowTaskId=" + workFlowTaskId +
                ", workFlowTaskType=" + workFlowTaskType +
                ", roleId=" + roleId +
                ", workFlowTaskStatus=" + workFlowTaskStatus +
                ", taskDealUserId=" + taskDealUserId +
                ", taskDealUserName=" + taskDealUserName +
                ", taskDealTime=" + taskDealTime +
                ", taskDealOperation=" + taskDealOperation +
                ", approvedOpinion=" + approvedOpinion +
                ", isActive=" + isActive +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", modifiedBy=" + modifiedBy +
                ", modifiedTime=" + modifiedTime +
                "}";
    }
}
