package com.ciicsh.gto.salarymanagement.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-07-11 14:49
 * @description 客户服务_配置变更任务记录表
 */
@Getter
@Setter
@ToString
@TableName("pr_payroll_config_task")
public class PrPayrollConfigTaskPO extends Model<PrPayrollConfigTaskPO> implements Serializable {

    // 序列化实体类
    private static final long serialVersionUID = 5553452477711720005L;

    // 指定主键
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    // 主键ID
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 变更任务单编号：SP+GL+YY+5位数字(36进制)
    @TableField(value = "config_task_id")
    private String configTaskId;

    // 管理方id
    @TableField(value = "management_id")
    private String managementId;

    // 任务单状态 1 - 未处理；2 - 已处理
    @TableField(value = "status")
    private int status;

    // 任务单类型 1 - 薪资组变更; 预留扩展类型,目前只有薪资组变更
    @TableField(value = "type")
    private int type;

    // 需要变更薪资组id
    @TableField(value = "target_id")
    private String targetId;

    // 变更说明
    @TableField(value = "remark")
    private String remark;

    // 是否有效
    @TableField(value = "is_active")
    private Boolean isActive;

    // 数据创建时间
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    // 最后修改时间
    @TableField(value = "modified_time", fill = FieldFill.UPDATE)
    private LocalDateTime modifiedTime;

    // 创建人
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    // 最后修改人
    @TableField(value = "modified_by", fill = FieldFill.UPDATE)
    private String modifiedBy;

    // 薪资组编码
    @TableField(exist = false)
    private String groupCode;

    @TableField(exist = false)
    // 薪资组名称
    private String groupName;

}
