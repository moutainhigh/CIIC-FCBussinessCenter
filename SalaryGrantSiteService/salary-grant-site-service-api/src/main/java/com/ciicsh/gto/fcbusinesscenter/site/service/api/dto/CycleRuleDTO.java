package com.ciicsh.gto.fcbusinesscenter.site.service.api.dto;

import java.io.Serializable;

/**
 * <p>
 * 服务周期规则信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-06
 */
public class CycleRuleDTO extends CommonListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务周期规则ID
     */
    private Integer cycleRuleId;
    /**
     * 管理方编号
     */
    private String managementId;
    /**
     * 薪资发放日_日
     */
    private String salaryDayDate;
    /**
     * 薪资发放日_时段：1上午 2下午
     */
    private String salaryDayTime;

    public Integer getCycleRuleId() {
        return cycleRuleId;
    }

    public void setCycleRuleId(Integer cycleRuleId) {
        this.cycleRuleId = cycleRuleId;
    }

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public String getSalaryDayDate() {
        return salaryDayDate;
    }

    public void setSalaryDayDate(String salaryDayDate) {
        this.salaryDayDate = salaryDayDate;
    }

    public String getSalaryDayTime() {
        return salaryDayTime;
    }

    public void setSalaryDayTime(String salaryDayTime) {
        this.salaryDayTime = salaryDayTime;
    }
}
