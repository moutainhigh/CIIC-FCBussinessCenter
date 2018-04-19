package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 雇员变更日志表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-07
 */
public class EmployeeChangeLogDTO extends CommonListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 变更日志编号
     */
    private Long employeeChangeLogId;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 变更表名
     */
    private String changeTableName;
    /**
     * 变更主键id
     */
    private Long changeTableId;
    /**
     * 变更字段
     */
    private String changeField;
    /**
     * 修改值
     */
    private String changeValue;
    /**
     * 变更类型
     */
    private String changeType;
    /**
     * 更改操作:0-修改、1-删除、2-新增
     */
    private Integer changeOperation;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * 创建时间
     */
    private Date createdTime;

    public Long getEmployeeChangeLogId() {
        return employeeChangeLogId;
    }

    public void setEmployeeChangeLogId(Long employeeChangeLogId) {
        this.employeeChangeLogId = employeeChangeLogId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getChangeTableName() {
        return changeTableName;
    }

    public void setChangeTableName(String changeTableName) {
        this.changeTableName = changeTableName;
    }

    public Long getChangeTableId() {
        return changeTableId;
    }

    public void setChangeTableId(Long changeTableId) {
        this.changeTableId = changeTableId;
    }

    public String getChangeField() {
        return changeField;
    }

    public void setChangeField(String changeField) {
        this.changeField = changeField;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Integer getChangeOperation() {
        return changeOperation;
    }

    public void setChangeOperation(Integer changeOperation) {
        this.changeOperation = changeOperation;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
