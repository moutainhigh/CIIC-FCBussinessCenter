package com.ciicsh.gto.salarymanagement.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by houwanhua on 2017/12/15.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollAccountSetExtensionPO {
    private Integer id;
    private String managementId;
    private String accountSetName;
    private String accountSetCode;
    private Boolean ifGroupTemplate;
    private String payrollGroupCode;
    private String payrollGroupName;
    private String payrollGroupTemplateCode;
    private String payrollGroupTemplateName;
    private String empGroupCode;
    private String empGroupName;
    private Integer startDay;
    private Integer endDay;
    private Integer payrollPeriod;
    private String workCalendarName;
    private String workCalendarValue;
    private String remark;
    private Boolean isActive;
    private Date createdTime;
    private Date modifiedTime;
    private String createdBy;
    private String modifiedBy;
}
