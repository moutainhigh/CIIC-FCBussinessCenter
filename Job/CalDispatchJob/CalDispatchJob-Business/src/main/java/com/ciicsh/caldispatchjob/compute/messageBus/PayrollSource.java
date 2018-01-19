package com.ciicsh.caldispatchjob.compute.messageBus;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public interface PayrollSource {


    String PR_COMPUTE_OUTPUT = "pr_compute-output-channel";

    @Output(PR_COMPUTE_OUTPUT)
    MessageChannel computeStatusOutput();

}
