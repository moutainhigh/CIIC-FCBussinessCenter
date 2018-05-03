package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 配置表，薪酬账套信息
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollAccountSetDTO {


    private static final long serialVersionUID = 1L;

    /**
     * 自增薪资账套ID
     */
    private Integer id;
    /**
     * 所属管理方ID
     */
    private String managementId;
    /**
     * 薪酬账套名称
     */
    private String accountSetName;
    /**
     * 薪酬账套代码
     */
    private String accountSetCode;

    /**
     * 是否是薪资组模板
     */
    private Boolean ifGroupTemplate;

    /**
     * 薪资组编码
     */
    private String payrollGroupCode;
    /**
     * 薪资组模版编码
     */
    private String payrollGroupTemplateCode;
    /**
     * 雇员组编码
     */
    private String empGroupCode;
    /**
     * 工资期间开始日
     */
    private Integer startDay;
    /**
     * 工资期间结束日
     */
    private Integer endDay;
    /**
     * 枚举类型：
     0 表示 本月，
     1 表示 上月，
     2 表示 下月
     */
    private Integer payrollPeriod;
    /**
     * 工作日历名称
     */
    private String workCalendarName;
    /**
     * 工作日历值
     */
    private String workCalendarValue;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否有效
     */
    private Boolean isActive;
    /**
     * 数据创建时间
     */
    private Date createdTime;
    /**
     * 最后修改时间
     */
    private Date modifiedTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 最后修改人
     */
    private String modifiedBy;

}
