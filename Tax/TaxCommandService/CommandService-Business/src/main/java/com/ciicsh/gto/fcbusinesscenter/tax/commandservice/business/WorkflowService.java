package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import java.util.Map;

/**
 * 工作流服务
 * @author wuhua
 */
public interface WorkflowService {


    enum Process{
        fc_tax_main_task_audit;
    }

    //启动流程
    boolean startProcess(String missionId , Process process , Map<String, Object> variables);

    //完成任务
    boolean completeTaskByMissionId(String missionId , Map<String, Object> variables);

    //完成任务
    boolean completeTaskByTaskId(String taskId , Map<String, Object> variables);

}
