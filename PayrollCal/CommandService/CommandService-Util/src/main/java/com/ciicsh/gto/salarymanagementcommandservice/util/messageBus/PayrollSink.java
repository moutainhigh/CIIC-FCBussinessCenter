package com.ciicsh.gto.salarymanagementcommandservice.util.messageBus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public interface PayrollSink {
    String INPUT = "payroll-input-channel";

    @Input(INPUT)
    MessageChannel input();

    String COMPUTE_INPUT = "pr_compute-input-channel";

    @Input(COMPUTE_INPUT)
    MessageChannel computeStatusInput();

    // TO BE DELETED
    String MANAGEMENT_INPUT = "pr_management-input-channel";

    // TO BE DELETED
    @Input(MANAGEMENT_INPUT)
    MessageChannel managementInput();
}
