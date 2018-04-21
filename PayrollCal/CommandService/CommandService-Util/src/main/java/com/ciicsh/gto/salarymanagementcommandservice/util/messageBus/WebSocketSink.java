package com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */

@Service
public interface PayCalSink {

    String COMPUTE_STATUS_INPUT = "pr_compute-status-input-channel";

    // TO BE DELETED
    String MANAGEMENT_INPUT = "pr_management-input-channel";

    // TO BE DELETED
    @Input(MANAGEMENT_INPUT)
    MessageChannel managementInput();

    @Input(COMPUTE_STATUS_INPUT)
    SubscribableChannel computeStatusInput();
}
