package com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus;

import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.salarymanagement.entity.message.AdjustBatchMsg;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollEmpGroup;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/10/21.
 */
@EnableBinding(value = PayrollSource.class)
@Component
public class KafkaSender {

    private final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    private PayrollSource payrollSource;

    public void Send(PayrollMsg payrollMsg)
    {
        logger.info(String.format("发送正常批次消息：%s",payrollMsg.toString()));

        payrollSource.normalBatchOutput().send(MessageBuilder.withPayload(payrollMsg).build());
    }

    public void SendEmpGroup(PayrollEmpGroup empGroup)
    {
        logger.info(String.format("发送雇员组消息：%s",empGroup.toString()));

        payrollSource.empGroupOutput().send(MessageBuilder.withPayload(empGroup).build());
    }

    public void SendComputeAction(ComputeMsg computeMsg){
        try {
            logger.info(String.format("发送计算消息：%s",computeMsg.toString()));
            payrollSource.computeOutput().send(MessageBuilder.withPayload(computeMsg).build());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    /**
     * 发送计算状态完成
     * @param computeMsg
     */
    public void SendComputeCompleteAction(ComputeMsg computeMsg){
        try {
            logger.info(String.format("发送计算消息：%s",computeMsg.toString()));
            payrollSource.compCompleteOutput().send(MessageBuilder.withPayload(computeMsg).build());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    /**
     * 发送调整批次新增／删除
     * @param adjustBatchMsg
     */
    public void SendAdjustBatch(AdjustBatchMsg adjustBatchMsg){
        try {
            logger.info("发送调整批次消息");
            payrollSource.adujstBatchOutput().send(MessageBuilder.withPayload(adjustBatchMsg).build());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }


    /**
     * 发送薪资关帐
     * @param closingMsg
     */
    public void SendComputeClose(ClosingMsg closingMsg ){
        try {
            logger.info("发送关帐消息");
            payrollSource.computeCloseOutput().send(MessageBuilder.withPayload(closingMsg).build());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    /**
     * 发送薪资取消关帐
     * @param cancelClosingMsg
     */
    public void SendComputeUnClose(CancelClosingMsg cancelClosingMsg ){
        try {
            logger.info("发送取消关帐消息");
            payrollSource.computeUnCloseOutput().send(MessageBuilder.withPayload(cancelClosingMsg).build());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }



    // TO BE DELETED
    public void SendManagement(String str) {
        payrollSource.managementOutput().send(MessageBuilder.withPayload(str).build());
    }

}
