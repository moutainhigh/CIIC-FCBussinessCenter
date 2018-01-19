package com.ciicsh.caldispatchjob.compute.messageBus;

import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
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

    public void SendComputeStatus(ComputeMsg computeMsg)
    {
        payrollSource.computeStatusOutput().send(MessageBuilder.withPayload(computeMsg).build());
    }

}