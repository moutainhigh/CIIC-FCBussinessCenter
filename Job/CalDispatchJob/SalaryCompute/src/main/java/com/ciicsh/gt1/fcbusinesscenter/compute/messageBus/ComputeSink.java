package com.ciicsh.gt1.fcbusinesscenter.compute.messageBus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 18/4/19.
 */
@Service
public interface ComputeSink {

    String PR_COMPUTE_INPUT = "pr_compute-input-channel";

    @Input(PR_COMPUTE_INPUT)
    SubscribableChannel PayrollComputeInput();
}
