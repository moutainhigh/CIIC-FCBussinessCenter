package com.ciicsh.gto.salarymanagementcommandservice.util.messageBus;

import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/10/21.
 */
@EnableBinding(value = PayrollSink.class)
@Component
public class KafkaReceiver {

    @Autowired
    private SimpMessagingTemplate template;

    private final static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    @StreamListener(PayrollSink.INPUT)
    public void receive(Message<PayrollSink> message){
        logger.info("received from message: " + message.getPayload().toString());
    }


    @StreamListener(PayrollSink.COMPUTE_INPUT)
    public void receiveComputeStatus(ComputeMsg message){
        logger.info("received message: " + message.toString());
        String batchCode = message.getBatchCode();
        int status = message.getComputeStatus();
        String dest = "/compute/status/" + batchCode; // 浏览器订阅的topic
        template.convertAndSend(dest,status);         // 转发消息给客户端
    }

}
