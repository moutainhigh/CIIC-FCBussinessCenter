package com.ciicsh.caldispatchjob.compute.messageBus;

import com.ciicsh.caldispatchjob.compute.service.*;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollEmpGroup;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by bill on 17/10/21.
 */
@EnableBinding(value = PayrollSink.class)
@Component
public class PayrollReceiver {

    private final static Logger logger = LoggerFactory.getLogger(PayrollReceiver.class);

    @Autowired
    private EmpGroupServiceImpl empGroupService;

    @Autowired
    private NormalBatchServiceImpl batchService;

    @Autowired
    private AdjustBatchServiceImpl adjustBatchService;

    @Autowired
    private BackTraceBatchServiceImpl backTraceBatchService;

    @Autowired
    private ComputeServiceImpl computeService;

    @StreamListener(PayrollSink.INPUT)
    public void receive(PayrollMsg message){
        logger.info("received from batchCode : " + message.getBatchCode());
        processBatchInfo(message.getBatchCode(), message.getOperateType(),message.getBatchType());
    }

    @StreamListener(PayrollSink.EMP_GROUP_INPUT)
    public void receive(PayrollEmpGroup message){
        //logger.info("received employee group from message: " + message.getIds().size());
        processEmployees(message);

    }


    @StreamListener(PayrollSink.PR_COMPUTE_INPUT)
    public void receive(ComputeMsg computeMsg){
        logger.info("received payroll compute from message: " + computeMsg);
        processPayrollCompute(computeMsg.getBatchCode(),computeMsg.getBatchType());
        return;
    }

    /*
    @StreamListener(PayrollSink.PR_COMPUTE_INPUT)
    public void receive(Message<ComputeMsg> message){

        ComputeMsg computeMsg = message.getPayload();
        logger.info("received payroll compute from message: " + computeMsg);
        processPayrollCompute(computeMsg.getBatchCode(),computeMsg.getBatchType());
        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if (acknowledgment != null) {
            logger.info("Acknowledgment provided");
            acknowledgment.acknowledge();
        }
    }*/

    /**
     * 订阅雇员组里面的雇员列表变化：新增或者删除
     * @param message
     */
    private void processEmployees(PayrollEmpGroup message){
        List<String> empIds = message.getIds();
        List<String> groupIds = message.getEmpGroupIds();

        logger.info("Opt Operation Type :" + message.getOperateType());

        if(message.getOperateType() == OperateTypeEnum.ADD.getValue()){
            String empGroupId = String.valueOf(groupIds.toArray()[0]);
            logger.info("ADD Opt");
            logger.info("emp group id:" + empGroupId);
            empGroupService.batchInsertOrUpdateGroupEmployees(empGroupId,empIds);
        }
        else if(message.getOperateType() == OperateTypeEnum.DELETE.getValue()){
            logger.info("DELETE Opt");
            empGroupService.batchDelGroupEmployees(groupIds,empIds);
        }

    }

    /**
     * 订阅批次：新增或者删除
     * @param batchCode
     * @param optType
     */
    private void processBatchInfo(String batchCode, int optType, int batchType){
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            if (optType == OperateTypeEnum.ADD.getValue()) {
                batchService.batchInsertOrUpdateNormalBatch(batchCode);
            } else if (optType == OperateTypeEnum.DELETE.getValue()) {
                batchService.deleteNormalBatch(batchCode);
            }
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()){
            if (optType == OperateTypeEnum.DELETE.getValue()) {
                adjustBatchService.deleteAdjustBatch(batchCode);
            }
        }else {
            if (optType == OperateTypeEnum.DELETE.getValue()) {
                backTraceBatchService.deleteBackTraceBatch(batchCode);
            }
        }
    }

    /**
     * 接收薪资计算消息
     * @param batchCode
     */
    private void processPayrollCompute(String batchCode,int batchType){
        computeService.processCompute(batchCode,batchType);
    }
}
