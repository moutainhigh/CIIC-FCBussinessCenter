package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import java.util.Map;

/**
 * <p>
 * 薪资发放任务单 工作流发起请求DTO
 * </p>
 *
 * @author gaoyang
 * @since 2018-1-25
 */
public class SalaryGrantTaskMissionRequestDTO {

    private String missionId;
    private String processDefinitionKey;
    private Map<String,Object> variables;

    public SalaryGrantTaskMissionRequestDTO() {
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "SalaryGrantTaskMissionRequestDTO{" +"missionId='" + missionId + ", processDefinitionKey='" + processDefinitionKey + ", variables=" + variables +'}';
    }
}
