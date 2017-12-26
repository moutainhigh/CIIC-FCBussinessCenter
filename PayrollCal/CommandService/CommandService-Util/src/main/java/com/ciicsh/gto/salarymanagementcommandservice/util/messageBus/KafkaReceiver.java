package com.ciicsh.gto.salarymanagementcommandservice.util.messageBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/10/21.
 */
@EnableBinding(value = PayrollSink.class)
@Component
public class KafkaReceiver {

    private final static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    @StreamListener(PayrollSink.INPUT)
    public void receive(Message<PayrollSink> message){
        logger.info("received from message: " + message.getPayload().toString());
    }

    /*
    @StreamListener(CustCommandSink.INPUT1)
    public void receive1(Customer customer){
        System.out.println("customer Received : " + customer);
    }

    @StreamListener(CustCommandSink.INPUT2)
    public void receive2(Customer customer){
        System.out.println("customer Received : " + customer);
    }
    */

}
