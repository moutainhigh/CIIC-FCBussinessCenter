package com.ciicsh.gto.fcbusinesscenter.site.service.api.dto;

import java.util.Map;

/**
 * <p>
 * 薪资发放任务单 工作流任务处理请求DTO
 * </p>
 *
 * @author gaoyang
 * @since 2018-1-25
 */
public class SalaryGrantTaskRequestDTO {
    private String taskId;
    private String assignee;
    private Map<String,Object> variables;

    public SalaryGrantTaskRequestDTO() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "SalaryGrantTaskRequestDTO{" +"taskId='" + taskId + ", assignee='" + assignee + ", variables=" + variables +'}';
    }
}
