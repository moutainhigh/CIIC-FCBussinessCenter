package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.messagebus;

import com.alibaba.fastjson.JSON;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskProcessService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.EmployeeReturnTicketDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
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
    /**
     * 记录日志
     */
    @Autowired
    LogClientService logClientService;
    @Autowired
    private SalaryGrantTaskProcessService salaryGrantTaskProcessService;
    @Autowired
    private SalaryGrantEmployeeCommandService salaryGrantEmployeeCommandService;
    @Autowired
    private SalaryGrantTaskQueryService salaryGrantTaskQueryService;

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

    @StreamListener(MsgConstants.FC.PAYROLL_MAIN)
    public void salaryGrantMainTaskCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
       // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(MsgConstants.FC.PAYROLL_LOCAL_DOMESTIC_CURRENCY)
    public void salaryGrantSubTaskLTBCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
        // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(MsgConstants.FC.PAYROLL_LOCAL_FOREIGN_CURRENCY)
    public void salaryGrantSubTaskLTWCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
        // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(MsgConstants.FC.PAYROLL_NONLOCAL)
    public void salaryGrantSubTaskSTCreateWorkFlow(Message<TaskCreateMsgDTO> message){
        TaskCreateMsgDTO taskMsgDTO = message.getPayload();
        String returnInfo = taskMsgDTO.toString();
        //todo 把消息返回的任务信息插入到任务日志表中
        // logger.info("收到消息from BASE_ADJUST_YEARLY_NONLOCAL-useWork: " + returnInfo);
    }

    @StreamListener(MsgConstants.FC.SUPPLIER_PAYMENT)
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
    @Transactional(rollbackFor = Exception.class)
    @StreamListener(TaskSink.SALARY_GRANT_REFUND)
    public void salaryGrantRefundProcess(Message<PayApplyReturnTicketDTO> message){
        PayApplyReturnTicketDTO payApplyReturnTicketDTO = message.getPayload();
        if (!ObjectUtils.isEmpty(payApplyReturnTicketDTO)) {
            //发放批次号
            String taskCode = payApplyReturnTicketDTO.getSequenceNo();
            //退票雇员信息列表
            List<EmployeeReturnTicketDTO> refundEmployeeList = payApplyReturnTicketDTO.getEmployeeReturnTicketDTOList();

            salaryGrantEmployeeCommandService.updateForRefund(taskCode, refundEmployeeList);
        }
    }

    /**
     * 结算中心支付消息
     * @param message
     */
    @StreamListener(TaskSink.SALARY_GRANT_PAYMENT)
    public void salaryGrantPaymentProcess(Message<PayApplyPayStatusDTO> message) {
        PayApplyPayStatusDTO dto = message.getPayload();
        try {
            if (!ObjectUtils.isEmpty(dto) && !ObjectUtils.isEmpty(dto.getPayApplySalaryDTOList()) && dto.getPayApplySalaryDTOList().size()>0 && SalaryGrantBizConsts.SETTLEMENT_CENTER_BUSINESS_TYPE_SG.equals(dto.getBusinessType()) && SalaryGrantBizConsts.SETTLEMENT_CENTER_PAY_STATUS_SUCCESS.equals(dto.getPayStatus())) {
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步结算中心支付状态 --> start").setContent(JSON.toJSONString(dto)));
                salaryGrantTaskQueryService.syncPayStatus(dto.getPayApplySalaryDTOList());
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步结算中心支付状态 --> end"));
            }
        } catch (Exception e) {
            Map map = new HashMap();
            map.put("PayApplyPayStatusDTO", JSON.toJSONString(dto));
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步结算中心支付状态 --> exception").setContent(e.getMessage()).setTags(map));
        }
    }

    /**
     * 接收计算引擎取消关账消息，获取计算批次号
     * @param message
     */
    @StreamListener(TaskSink.SALARY_GRANT_MAIN_TASK_CANCEL_TASK)
    public void salaryGrantMainTaskCancelTask(Message<CancelClosingMsg> message) {
        CancelClosingMsg cancelClosingMsg = message.getPayload();
        try {
            if (!ObjectUtils.isEmpty(cancelClosingMsg)) {
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("取消关账 --> start").setContent(JSON.toJSONString(cancelClosingMsg)));
                salaryGrantTaskQueryService.cancelClosing(cancelClosingMsg);
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("取消关账 --> end"));
            }
        } catch (Exception e) {
            Map map = new HashMap();
            map.put("CancelClosingMsg", JSON.toJSONString(cancelClosingMsg));
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("取消关账 --> exception").setContent(e.getMessage()).setTags(map));
        }
    }
}