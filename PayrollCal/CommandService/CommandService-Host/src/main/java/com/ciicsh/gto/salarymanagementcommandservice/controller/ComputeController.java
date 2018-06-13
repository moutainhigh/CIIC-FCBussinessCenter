package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagement.entity.message.wsComputeMsg;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus.KafkaSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * Created by bill on 18/1/10.
 */
@Controller
public class ComputeController { //websocket long connection

    private final static Logger logger = LoggerFactory.getLogger(ComputeController.class);

    @Autowired
    private KafkaSender sender;

    @Autowired
    private PrNormalBatchService batchService;

    @Autowired
    private PrAdjustBatchService adjustBatchService;

    @Autowired
    private PrBackTrackingBatchService backTrackingBatchService;

    @MessageMapping("/getComputeStatus")
    public void getComputeStatus(wsComputeMsg computeMsg) throws Exception {
        String batchCode = computeMsg.getBatchCode();
        int batchType = computeMsg.getBatchType();
        //String dest = "/compute/status/" + computeMsg.getBatchCode();

        int rowAffected = 0;

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = batchService.auditBatch(batchCode, "", BatchStatusEnum.COMPUTING.getValue(), "sys","","");
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()){
            rowAffected = adjustBatchService.auditBatch(batchCode,"", BatchStatusEnum.COMPUTING.getValue(), "sys","","");
        }else if(batchType == BatchTypeEnum.BACK.getValue()) {
            rowAffected = backTrackingBatchService.auditBatch(batchCode, "", BatchStatusEnum.COMPUTING.getValue(), "sys", "","");
        }

        if(rowAffected > 0) {
            ComputeMsg msg = new ComputeMsg();
            msg.setBatchCode(batchCode);
            msg.setBatchType(batchType);
            sender.SendComputeAction(msg); // send message to kafka
            logger.info(" 发送薪资计算消息到kafka : " + batchCode + "－－批次类型号：" + String.valueOf(batchType));
        }

        /*for(int i=0; i<1000; i++ ) {
            Thread.sleep(1*1000);
            template.convertAndSend(dest, i);
        }*/

    }

}
