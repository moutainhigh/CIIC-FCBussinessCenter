package com.ciicsh.caldispatchjob.compute.messageBus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public interface PayrollSink {
    String INPUT = "payroll-input-channel";

    String EMP_GROUP_INPUT= "pr_emp_group-channel";

    @Input(INPUT)
    MessageChannel input();

    @Input(EMP_GROUP_INPUT)
    MessageChannel empGroupInput();
}
