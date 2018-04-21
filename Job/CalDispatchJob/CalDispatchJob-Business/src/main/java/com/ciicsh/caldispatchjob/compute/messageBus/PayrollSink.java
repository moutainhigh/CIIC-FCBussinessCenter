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

    String PR_NORMAL_BATCH_INPUT = "pr-normal-batch-input-channel";

    String EMP_GROUP_INPUT= "pr_emp_group-channel";

    String PR_COMPUTE_COMPLTE_INPUT = "pr_compute-complete-input-channel";

    String PR_ADJUST_BATCH_INPUT = "pr_adjust_batch-input-channel";

    @Input(PR_NORMAL_BATCH_INPUT)
    MessageChannel input();

    @Input(EMP_GROUP_INPUT)
    MessageChannel empGroupInput();

    @Input(PR_COMPUTE_COMPLTE_INPUT)
    SubscribableChannel ComputeCompleteInput();

    @Input(PR_ADJUST_BATCH_INPUT)
    SubscribableChannel AdjustBatchInput();
}
