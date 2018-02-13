package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author wuhua
 */
public class RequestForTaskSubDeclare extends PageInfo {

    /**
     * 主键ID
     */
    private Long id;

    private Long[] taskSubDeclareId;

    /**
     * 批量完成/批量退回申报子任务ID
     */
    private String[] subDeclareIds;

    /**
     * 缴纳账户
     */
    private String declareAccount;

    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 页签状态类别handling,completed,retreated,failed
     */
    private String statusType;

    /**
     * 个税期间
     */
    private String period;

    /**
     * 页签类型：currentPan,currentBeforePan,currentAfterPan
     */
    private String periodType;

    /**
     * 修改人
     */
    private String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long[] getTaskSubDeclareId() {
        return taskSubDeclareId;
    }

    public void setTaskSubDeclareId(Long[] taskSubDeclareId) {
        this.taskSubDeclareId = taskSubDeclareId;
    }

    public String[] getSubDeclareIds() {
        return subDeclareIds;
    }

    public void setSubDeclareIds(String[] subDeclareIds) {
        this.subDeclareIds = subDeclareIds;
    }

    public String getDeclareAccount() {
        return declareAccount;
    }

    public void setDeclareAccount(String declareAccount) {
        this.declareAccount = declareAccount;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
