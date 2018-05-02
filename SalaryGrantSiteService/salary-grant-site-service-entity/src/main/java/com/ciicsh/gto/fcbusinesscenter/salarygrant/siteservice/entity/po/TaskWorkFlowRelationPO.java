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
 * 任务单流程映射表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-02
 */
@TableName("sg_task_work_flow_relation")
public class TaskWorkFlowRelationPO extends Model<TaskWorkFlowRelationPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 映射ID
     */
    @TableId(value="task_work_flow_relation_id", type= IdType.AUTO)
    private Long taskWorkFlowRelationId;
    /**
     * 任务单编号
     */
    @TableField("task_code")
    private String taskCode;
    /**
     * 流程编号
     */
    @TableField("work_flow_process_id")
    private String workFlowProcessId;
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


    public Long getTaskWorkFlowRelationId() {
        return taskWorkFlowRelationId;
    }

    public void setTaskWorkFlowRelationId(Long taskWorkFlowRelationId) {
        this.taskWorkFlowRelationId = taskWorkFlowRelationId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getWorkFlowProcessId() {
        return workFlowProcessId;
    }

    public void setWorkFlowProcessId(String workFlowProcessId) {
        this.workFlowProcessId = workFlowProcessId;
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
        return this.taskWorkFlowRelationId;
    }

    @Override
    public String toString() {
        return "TaskWorkFlowRelationPO{" +
                ", taskWorkFlowRelationId=" + taskWorkFlowRelationId +
                ", taskCode=" + taskCode +
                ", workFlowProcessId=" + workFlowProcessId +
                ", isActive=" + isActive +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", modifiedBy=" + modifiedBy +
                ", modifiedTime=" + modifiedTime +
                "}";
    }
}
