package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagement.entity.message.wsComputeMsg;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by bill on 18/1/10.
 */
@Controller
public class ComputeController { //websocket long connection

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private KafkaSender sender;

    @MessageMapping("/getComputeStatus")
    public void getComputeStatus(wsComputeMsg computeMsg) throws Exception {
        String batchCode = computeMsg.getBatchCode();
        System.out.println("get code " + computeMsg.getBatchCode());
        //String dest = "/compute/status/" + computeMsg.getBatchCode();
        ComputeMsg msg = new ComputeMsg();
        msg.setBatchCode(batchCode);
        sender.SendComputeAction(msg); // send message to kafka
        /*for(int i=0; i<1000; i++ ) {
            Thread.sleep(1*1000);
            template.convertAndSend(dest, i);
        }*/

    }

}
