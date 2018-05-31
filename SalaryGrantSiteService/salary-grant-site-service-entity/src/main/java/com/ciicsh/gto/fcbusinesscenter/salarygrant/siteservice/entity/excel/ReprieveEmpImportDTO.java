package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.excel;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * 暂缓雇员导入信息
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-16
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class ReprieveEmpImportDTO {
    /**
     * 薪资发放暂缓名单编号
     */
    @TableId("salarygrant_reprieve_employee_import_id")
    private Long salarygrantReprieveEmployeeImportId;
    /**
     * 任务单编号
     */
    @TableField("task_code")
    private String taskCode;
    /**
     * 任务单类型:0-主表、1-中智大库、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
     */
    @TableField("task_type")
    private Integer taskType;
    /**
     * 雇员编号
     */
    @TableField("employee_id")
    private String employeeId;
    /**
     * 雇员名称
     */
    @TableField("employee_name")
    private String employeeName;
    /**
     * 导入时间
     */
    @TableField("import_time")
    private Date importTime;
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
}
