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
}
