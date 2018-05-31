package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.messagebus;

import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskProcessService;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayApplyPayStatusDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayApplyReturnTicketDTO;
import com.ciicsh.gto.sheetservice.api.MsgConstants;
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
    private SalaryGrantTaskProcessService salaryGrantTaskProcessService;

    /**
     * 接收计算引擎关账消息，获取计算批次号
     * @param message
     */
    @StreamListener(TaskSink.SALARY_GRANT_MAIN_TASK_CREATE_TASK)
    public void salaryGrantMainTaskCreateTask(Message<ClosingMsg> message){
        // 1、接收消息返回的计算批次号
        String batchCode = message.getPayload().getBatchCode();
        // 接收消息返回的批次类型
        Integer batchType = message.getPayload().getBatchType();
        Map batchParam = new HashMap();
        batchParam.put("batchCode",batchCode);
        batchParam.put("batchType",batchType);
        // 2、根据计算批次号查询批次业务表的批次数据信息，包括计算结果数据及雇员服务协议信息。发起薪资发放任务单。
        salaryGrantTaskProcessService.createSalaryGrantMainTask(batchParam);

    }

    @StreamListener(TaskSink.SALARY_GRANT_MAIN_TASK_CREATE_WORK_FLOW)
    public void salaryGrantMainTaskCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
       // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(TaskSink.SALARY_GRANT_SUB_TASK_LTB_CREATE_WORK_FLOW)
    public void salaryGrantSubTaskLTBCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
        // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(TaskSink.SALARY_GRANT_SUB_TASK_LTW_CREATE_WORK_FLOW)
    public void salaryGrantSubTaskLTWCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
        // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(TaskSink.SALARY_GRANT_SUB_TASK_ST_CREATE_WORK_FLOW)
    public void salaryGrantSubTaskSTCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
        // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(TaskSink.SALARY_GRANT_SUPPLIER_PAYMENT_TASK_CREATE_WORK_FLOW)
    public void salaryGrantSupplierPaymentTaskCreateWorkFlow(Message<TaskCreateMsgDTO> message){
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

    /**
     * 结算中心退票消息
     * @param message
     */
    @StreamListener(TaskSink.SALARY_GRANT_REFUND)
    public void salaryGrantRefundProcess(Message<PayApplyReturnTicketDTO> message){

    }

    /**
     * 结算中心支付消息
     * @param message
     */
    @StreamListener(TaskSink.SALARY_GRANT_PAYMENT)
    public void salaryGrantPaymentProcess(Message<PayApplyPayStatusDTO> message){

    }

    /**
     * 接收计算引擎取消关账消息，获取计算批次号
     * @param message
     */
    @StreamListener(TaskSink.SALARY_GRANT_MAIN_TASK_CANCEL_TASK)
    public void salaryGrantMainTaskCancelTask(Message<CancelClosingMsg> message){

    }
}
