package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.messagebus;

import com.alibaba.fastjson.JSON;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantWorkFlowEnums;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskProcessService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.WorkFlowTaskInfoService;
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
import com.github.pagehelper.StringUtil;
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
    @Autowired
    private WorkFlowTaskInfoService workFlowTaskInfoService;

    /**
     * 接收计算引擎关账消息，获取计算批次号
     * @param message
     */
    @StreamListener(TaskSink.SALARY_GRANT_MAIN_TASK_CREATE_TASK)
    public void salaryGrantMainTaskCreateTask(Message<ClosingMsg> message) {
        ClosingMsg closingMsg = message.getPayload();
        try{
            if(!ObjectUtils.isEmpty(closingMsg)){
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("关账 --> start").setContent(JSON.toJSONString(closingMsg)));
                String batchCode = closingMsg.getBatchCode();
                Integer batchType = closingMsg.getBatchType();
                Long version = closingMsg.getVersion();
                if(StringUtil.isEmpty(batchCode) || batchType == null || version == null){
                    logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("关账").setContent("关账传入参数缺失，生成薪资发放任务单失败！"));
                }else{
                    salaryGrantTaskProcessService.toClosing(closingMsg);
                }
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("关账 --> end"));
            }
        }catch (Exception e){
            Map map = new HashMap();
            map.put("ClosingMsg", JSON.toJSONString(closingMsg));
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("关账 --> exception").setContent(e.getMessage()).setTags(map));
        }
    }

    @StreamListener(TaskSink.PAYROLL_MAIN)
    public void salaryGrantMainTaskCreateWorkFlow(Message<TaskCreateMsgDTO> message) {
        this.handleMessage(SalaryGrantWorkFlowEnums.ProcessDefinitionKey.SGT.getType(), message);
    }

    @StreamListener(TaskSink.PAYROLL_LOCAL_DOMESTIC_CURRENCY)
    public void salaryGrantSubTaskLTBCreateWorkFlow(Message<TaskCreateMsgDTO> message) {
        this.handleMessage(SalaryGrantWorkFlowEnums.ProcessDefinitionKey.LTB.getType(), message);
    }

    @StreamListener(TaskSink.PAYROLL_LOCAL_FOREIGN_CURRENCY)
    public void salaryGrantSubTaskLTWCreateWorkFlow(Message<TaskCreateMsgDTO> message) {
        this.handleMessage(SalaryGrantWorkFlowEnums.ProcessDefinitionKey.LTW.getType(), message);
    }

    @StreamListener(TaskSink.PAYROLL_NONLOCAL)
    public void salaryGrantSubTaskSTACreateWorkFlow(Message<TaskCreateMsgDTO> message) {
        this.handleMessage(SalaryGrantWorkFlowEnums.ProcessDefinitionKey.STA.getType(), message);
    }

    @StreamListener(TaskSink.SUPPLIER_PAYMENT)
    public void salaryGrantSupplierPaymentTaskCreateWorkFlow(Message<TaskCreateMsgDTO> message) {
        this.handleMessage(SalaryGrantWorkFlowEnums.ProcessDefinitionKey.SPT.getType(), message);
    }

    /**
     * 接收任务完成消息
     * @param message
     */
    @StreamListener(TaskSink.COMMON_TASKSERVICE_TASK_COMPLETE)
    public void commonTaskServiceTaskComplete(Message<TaskCompleteMsgDTO> message) {
        try {
            //TaskCompleteMsgDTO  taskCompleteMsgDTO = message.getPayload();
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("任务完成 ").setContent(""));
            //workFlowTaskInfoService.taskComplete(taskCompleteMsgDTO);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("任务完成 --> exception").setContent(e.getMessage()));
        }
    }

    /**
     * 流程结束消息
     * @param message
     *
    @StreamListener(MsgConstants.COMMON_TASKSERVICE_PROCESS_COMPLETE)
    public void commonTaskServiceProcessComplete(Message<ProcessCompleteMsgDTO> message) {
        ProcessCompleteMsgDTO msgDto = message.getPayload();
        if (checkDefinitionKey(msgDto.getProcessDefinitionKey())) {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("流程结束 ").setContent("TEST"));
        }
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("流程结束 ").setContent(""));
    }*/
     @StreamListener(MsgConstants.COMMON_TASKSERVICE_PROCESS_COMPLETE)
    public void commonTaskServiceProcessComplete(Object message) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("流程结束 ").setContent(""));
    }

    /**
     * 结算中心退票消息
     * @param message
     */
    @Transactional(rollbackFor = Exception.class)
    @StreamListener(TaskSink.SALARY_GRANT_REFUND)
    public void salaryGrantRefundProcess(Message<PayApplyReturnTicketDTO> message) {
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

    private void handleMessage(String action, Message<TaskCreateMsgDTO> message) {
        try {
            TaskCreateMsgDTO taskMsgDTO = message.getPayload();
            if (!ObjectUtils.isEmpty(taskMsgDTO) && workFlowTaskInfoService.selectWfTaskInfoByTaskId(taskMsgDTO.getTaskId()) ==0) {
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放 --> 工作流：" + action).setTitle("start").setContent(JSON.toJSONString(taskMsgDTO)));
                workFlowTaskInfoService.createMessage(taskMsgDTO);
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放 --> 工作流：" + action).setTitle("end"));
            }
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放 --> 工作流：" + action).setTitle("exception").setContent(e.getMessage()));
        }
    }

    private Boolean checkDefinitionKey(String key) {
        final String definitionKey = "payroll_main,payroll_local_domestic_currency,payroll_local_foreign_currency,payroll_nonlocal,supplier_payment";
        return definitionKey.indexOf(key) != -1;
    }

}