package com.ciicsh.gto.salarymanagement.entity.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 薪资组版本历史记录表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-28
 */
@TableName("pr_payroll_group_history")
public class PrPayrollGroupHistoryPO extends Model<PrPayrollGroupHistoryPO> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**
     * 薪资组code
     */
    @TableField("payroll_group_code")
    private String payrollGroupCode;
    /**
     * 薪资组版本
     */
    private String version;
    /**
     * 该薪资组版本的详细数据
     */
    @TableField("payroll_group_history")
    private String payrollGroupHistory;
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

    public String getPayrollGroupCode() {
        return payrollGroupCode;
    }

    public void setPayrollGroupCode(String payrollGroupCode) {
        this.payrollGroupCode = payrollGroupCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPayrollGroupHistory() {
        return payrollGroupHistory;
    }

    public void setPayrollGroupHistory(String payrollGroupHistory) {
        this.payrollGroupHistory = payrollGroupHistory;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PrPayrollGroupHistory{" +
                "id=" + id +
                ", payrollGroupCode=" + payrollGroupCode +
                ", version=" + version +
                ", payrollGroupHistory=" + payrollGroupHistory +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                "}";
    }
}
