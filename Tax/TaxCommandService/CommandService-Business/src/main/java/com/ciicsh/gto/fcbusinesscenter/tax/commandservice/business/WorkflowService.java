package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import java.util.Map;

/**
 * 工作流服务
 * @author wuhua
 */
public interface WorkflowService {

    enum operationType{
        //启动工作流
        startProcess,
        //创建任务
        createTask,
        //完成任务
        completeTask,
        //完成工作流
        completeProcess;
    }

    //启动流程
    boolean startProcess(String missionId , String processDef , Map<String, Object> variables);

    //创建任务
    void createTask(String taskId,String taskType,String missionId,String processId,String processDefinitionKey,String assumer,String assumeType);

    //完成任务
    void completeTask(String missionId , Map<String, Object> variables);

}
