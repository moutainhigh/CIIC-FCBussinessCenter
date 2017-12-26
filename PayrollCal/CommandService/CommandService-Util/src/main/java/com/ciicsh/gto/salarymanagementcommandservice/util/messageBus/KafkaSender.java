package com.ciicsh.gto.salarymanagementcommandservice.util.messageBus;

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
        payrollSource.output().send(MessageBuilder.withPayload(payrollMsg).build());
    }

    public void SendEmpGroup(PayrollEmpGroup empGroup)
    {
        payrollSource.empGroupOutput().send(MessageBuilder.withPayload(empGroup).build());
    }

    /*
    public void Send1(Customer customer){
        custSource.output1().send(MessageBuilder.withPayload(customer).build());
    }

    public void Send2(String message){
        custSource.output2().send(MessageBuilder.withPayload(message).build());
    }
    */
}
