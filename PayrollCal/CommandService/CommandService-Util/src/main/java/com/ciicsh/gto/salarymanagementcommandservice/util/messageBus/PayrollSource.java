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
}