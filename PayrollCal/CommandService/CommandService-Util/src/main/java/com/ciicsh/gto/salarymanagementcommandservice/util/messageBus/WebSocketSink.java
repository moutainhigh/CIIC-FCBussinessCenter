package com.ciicsh.gto.salarymanagementcommandservice.util.messageBus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */

@Service
public interface WebSocketSink {

    String COMPUTE_STATUS_INPUT = "pr_compute-status-input-channel";

    @Input(COMPUTE_STATUS_INPUT)
    SubscribableChannel computeStatusInput();
}
