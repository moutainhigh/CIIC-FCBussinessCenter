package com.ciicsh.gto.fcbusinesscenter.site.service.host.messagebus;

import com.ciicsh.gto.entityidservice.api.EntityIdServiceProxy;
import com.ciicsh.gto.fcbusinesscenter.site.service.business.SalaryGrantMainTaskService;
import com.ciicsh.gto.sheetservice.api.MsgConstants;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.ProcessCompleteMsgDTO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCompleteMsgDTO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCreateMsgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 消息接收类
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-25
 */
@EnableBinding(value = TaskSink.class)
@Component
public class KafkaReceiver {

    @Autowired
    private SalaryGrantMainTaskService salaryGrantMainTaskService;

    //todo 接收计算引擎消息，获取计算批次号
    @StreamListener(TaskSink.SALARY_GRANT_MAIN_TASK_CREATE_TASK)
    public void salaryGrantMainTaskCreateTask(){
        // 1、接收消息返回的计算批次号
        String batchCode = null;
        // 接收消息返回的批次类型
        String batchType = null;
        Map batchParam = new HashMap();
        batchParam.put("batchCode",batchCode);
        batchParam.put("batchType",batchType);
        // 2、根据计算批次号查询批次业务表的批次数据信息，包括计算结果数据及雇员服务协议信息。发起薪资发放任务单。
        salaryGrantMainTaskService.createSalaryGrantMainTask(batchParam);

    }

    @StreamListener(TaskSink.SALARY_GRANT_MAIN_TASK_CREATE_WORK_FLOW)
    public void salaryGrantMainTaskCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
       // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    /**
     * 接收任务完成消息
     * @param message
     */
    @StreamListener(MsgConstants.COMMON_TASKSERVICE_TASK_COMPLETE)
    public void commonTaskserviceTaskComplete(Message<TaskCompleteMsgDTO> message){
        TaskCompleteMsgDTO taskCompleteMsgDTO = message.getPayload();
        String returnInfo = "taskId="+taskCompleteMsgDTO.getTaskId()+
                ",taskType="+taskCompleteMsgDTO.getTaskType()+
                ",missionId="+taskCompleteMsgDTO.getMissionId()+
                ",processId="+taskCompleteMsgDTO.getProcessId()+
                ",processDefinitionKey="+taskCompleteMsgDTO.getProcessDefinitionKey()+
                ",Variables="+taskCompleteMsgDTO.getVariables();
        //todo 把消息返回的任务信息插入到任务日志表中
       // logger.info("收到任务完成消息: " + returnInfo);
    }

    /**
     * 流程结束消息
     * @param message
     */
    @StreamListener(MsgConstants.COMMON_TASKSERVICE_PROCESS_COMPLETE)
    public void commonTaskserviceProcessComplete(Message<ProcessCompleteMsgDTO> message){
        ProcessCompleteMsgDTO processCompleteMsgDTO = message.getPayload();
        //todo 把消息返回的任务信息插入到任务日志表中
        String returnInfo = "processId="+processCompleteMsgDTO.getProcessId()+
                ",missionId="+processCompleteMsgDTO.getMissionId()+
                ",processDefinitionKey="+processCompleteMsgDTO.getProcessDefinitionKey();
        //logger.info("收到流程结束消息: " + returnInfo);
    }
}
