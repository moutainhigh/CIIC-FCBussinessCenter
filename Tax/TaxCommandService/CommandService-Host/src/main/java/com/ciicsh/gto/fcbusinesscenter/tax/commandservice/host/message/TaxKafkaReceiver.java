package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.MongodbService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.WorkflowService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskWorkflowHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskWorkflowHistoryPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayApplyPayStatusDTO;
import com.ciicsh.gto.sheetservice.api.dto.ProcessCompleteMsgDTO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCreateMsgDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yuantongqing on 2018/1/31
 */
@EnableBinding(value = TaxSink.class)
@Component
public class TaxKafkaReceiver {
    private final static Logger logger = LoggerFactory.getLogger(TaxKafkaReceiver.class);

    @Autowired
    private TaskSubMoneyService taskSubMoneyService;

    @Autowired
    private MongodbService mongodbService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private TaskMainMapper taskMainMapper;

    @Autowired
    private TaskMainService taskMainService;

    @Autowired
    private TaskWorkflowHistoryMapper taskWorkflowHistoryMapper;

    /**
     * 监听结算中心划款返回信息
     * @param payApplyPayStatusDTO
     */
    @StreamListener(TaxSink.PAY_APPLY_PAY_STATUS_STREAM)
    public void moneyMessage(PayApplyPayStatusDTO payApplyPayStatusDTO) {
        try {
            //上海个税类型为6
            if (payApplyPayStatusDTO.getBusinessType() == 6) {
                long subMoneyId = payApplyPayStatusDTO.getBusinessPkId();
                int status = payApplyPayStatusDTO.getPayStatus();
                TaskSubMoneyBO taskSubMoneyBO = new TaskSubMoneyBO();
                taskSubMoneyBO.setId(subMoneyId);
                if (status == 9) {
                    logger.info(subMoneyId + "划款成功");
                    //修改
                    taskSubMoneyBO.setPayStatus("02");
                    try {
                        taskSubMoneyService.updateTaskSubMoneyById(taskSubMoneyBO);
                    } catch (Exception e) {
                        logger.error("根据结算中心返回划款信息,更新状态为成功失败!",e);
                    }
                }
                if(status == -1){
                    logger.info(subMoneyId + "划款驳回");
                    taskSubMoneyBO.setPayStatus("03");
                    try {
                        taskSubMoneyService.updateTaskSubMoneyById(taskSubMoneyBO);
                    } catch (Exception e) {
                        logger.error("根据结算中心返回划款信息,更新状态为失败失败!",e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("结算中心划款返回信息异常!",e);
        }
    }

    /**
     * 计算引擎关账通知
     * @param closingMsg
     */
    @StreamListener(TaxSink.PR_COMPUTE_CLOSE_OUTPUT_CHANNEL)
    public void clMessage(ClosingMsg closingMsg) {
        try {
            if(closingMsg!=null && closingMsg.getBatchCode()!=null){
                mongodbService.acquireBatch(closingMsg);
            }
        } catch (Exception e) {
            logger.error("关账消息处理异常!",e);
        }
    }

    /**
     * 计算引擎取消关账通知
     * @param cancelClosingMsg
     */
    @StreamListener(TaxSink.PR_COMPUTE_UNCLOSE_OUTPUT_CHANNEL)
    public void unclMessage(CancelClosingMsg cancelClosingMsg) {
        try {
            mongodbService.cancelBatch(cancelClosingMsg);
        } catch (Exception e) {
            logger.error("计算引擎取消关账信息异常!",e);
        }
    }

    /**
     * 工作流通知(任务创建)
     * @param
     */
    @StreamListener(TaxSink.WORKFLOW_CHANNEL_TASK)
    public void wfTaskCompleteMessage(TaskCreateMsgDTO taskCreateMsgDTO) {

        try {
            if(taskCreateMsgDTO!=null){
                workflowService.createTask(taskCreateMsgDTO.getTaskId(),taskCreateMsgDTO.getTaskType(),taskCreateMsgDTO.getMissionId(),taskCreateMsgDTO.getProcessId()
                        ,taskCreateMsgDTO.getProcessDefinitionKey(),taskCreateMsgDTO.getAssumer(),taskCreateMsgDTO.getAssumeType()==null?null:taskCreateMsgDTO.getAssumeType().toString());
            }

        } catch (Exception e) {
            logger.error("工作流,创建任务失败!",e);
        }
    }


    /**
     * 工作流通知(流程结束)
     * @param
     */
    @StreamListener(TaxSink.WORKFLOW_CHANNEL_PROCESS_COMPLETE)
    public void wfProcessCompleteMessage(ProcessCompleteMsgDTO processCompleteMsgDTO) {

        try {
            if(processCompleteMsgDTO!=null){
                Map<String, Object> variables = processCompleteMsgDTO.getVariables();
                String taskNo = processCompleteMsgDTO.getMissionId();
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.eq("task_no",taskNo);
                List<TaskMainPO> list = taskMainMapper.selectList(wrapper);
                if(list!=null && list.size()>0){
                    TaskMainPO taskMainPO = list.get(0);
                    String taskMainId = taskMainPO.getId().toString();
                    String[] taskMainIds = new String[1]; taskMainIds[0] = taskMainId;
                    String status = taskMainPO.getStatus();
                    String[] statuses = new String[1]; statuses[0] = status;
                    String action = variables.get("action")==null? null:(String)variables.get("action");
                    if(StrKit.isNotEmpty(action)){
                        if(action.trim().equals("approval")){
                            taskMainService.updateTaskMainsStatus(taskMainIds,"02",statuses);
                        }else if(action.trim().equals("refuse")){
                            taskMainService.updateTaskMainsStatus(taskMainIds,"03",statuses);
                        }
                    }
                }
                TaskWorkflowHistoryPO taskWorkflowHistoryPO = new TaskWorkflowHistoryPO();
                taskWorkflowHistoryPO.setProcessDefinitionKey(processCompleteMsgDTO.getProcessDefinitionKey());//流程定义key
                taskWorkflowHistoryPO.setProcessId(processCompleteMsgDTO.getProcessId());//流程id
                taskWorkflowHistoryPO.setMissionId(processCompleteMsgDTO.getMissionId());//业务编号
                taskWorkflowHistoryPO.setOperationType(WorkflowService.operationType.completeProcess.toString());//处理类型
                taskWorkflowHistoryPO.setVariables(variables==null?null:variables.toString());//参数
                taskWorkflowHistoryMapper.insert(taskWorkflowHistoryPO);//记录流程创建信息
            }
        } catch (Exception e) {
            logger.error("流程结束异常!",e);
        }
    }
}
