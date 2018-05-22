package com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public interface PayrollSource {

    String PR_NORMAL_BATCH_INPUT = "pr-normal-batch-output-channel";

    @Output(PR_NORMAL_BATCH_INPUT)
    MessageChannel normalBatchOutput();

    String EMP_GROUP_OUTPUT = "pr_emp_group-output-channel";

    @Output(EMP_GROUP_OUTPUT)
    MessageChannel empGroupOutput();

    String PR_COMPUTE_OUTPUT = "pr_compute-output-channel";

    @Output(PR_COMPUTE_OUTPUT)
    MessageChannel computeOutput();

    String test = "pr_management-output-channel";

    @Output(test)
    MessageChannel managementOutput();

    String PR_COMPUTE_COMPLETE = "pr_compute-complete-output-channel";
    @Output(PR_COMPUTE_COMPLETE)
    MessageChannel compCompleteOutput();

    String PR_ADJUST_BATCH_OUTPUT = "pr_adjust_batch-output-channel";
    @Output(PR_ADJUST_BATCH_OUTPUT)
    MessageChannel adujstBatchOutput();

    String PR_COMPUTE_CLOSE_OUTPUT = "pr_compute-close-output-channel";
    @Output(PR_COMPUTE_CLOSE_OUTPUT)
    MessageChannel computeCloseOutput();

    String PR_COMPUTE_UNCLOSE_OUTPUT = "pr_compute-unclose-output-channel";
    @Output(PR_COMPUTE_UNCLOSE_OUTPUT)
    MessageChannel computeUnCloseOutput();
}
