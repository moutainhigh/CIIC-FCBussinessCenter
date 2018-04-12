package com.ciicsh.caldispatchjob.compute.messageBus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public interface PayrollSink {

    String INPUT = "payroll-input-channel";

    String EMP_GROUP_INPUT= "pr_emp_group-channel";

    String PR_COMPUTE_INPUT = "pr_compute-input-channel";

    String PR_COMPUTE_COMPLTE_INPUT = "pr_compute-complete-input-channel";

    @Input(INPUT)
    MessageChannel input();

    @Input(EMP_GROUP_INPUT)
    MessageChannel empGroupInput();

    @Input(PR_COMPUTE_INPUT)
    SubscribableChannel PayrollComputeInput();

    @Input(PR_COMPUTE_COMPLTE_INPUT)
    SubscribableChannel ComputeCompleteInput();
}
