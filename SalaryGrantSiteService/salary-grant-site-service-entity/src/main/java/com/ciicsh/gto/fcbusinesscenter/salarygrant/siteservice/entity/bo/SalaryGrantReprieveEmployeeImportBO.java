package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.*;

import java.util.Date;

/**
 * <p>
 * 薪资发放暂缓名单导入表
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantReprieveEmployeeImportBO {
    /**
     * 薪资发放暂缓名单编号
     */
    private Long salarygrantReprieveEmployeeImportId;
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 任务单类型:0-主表、1-中智大库、2-中智代发（委托机构）、3-客户自行、4-中智代发（客户账户）
     */
    private Integer taskType;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 导入时间
     */
    private Date importTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 最后修改时间
     */
    private Date createdTime;
    /**
     * 最后修改人
     */
    private String modifiedBy;
    /**
     * 最后修改时间
     */
    private Date modifiedTime;
}
