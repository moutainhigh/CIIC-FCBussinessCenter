package com.ciicsh.gto.salarymanagementcommandservice.util.messageBus;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public interface PayrollSource {

    String OUTPUT = "payroll-output-channel";

    @Output(OUTPUT)
    MessageChannel output();

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

}
