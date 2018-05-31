package com.ciicsh.caldispatchjob.compute.messageBus;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 18/5/23.
 */
@Service
public interface BatchSource {

    String PR_COMPUTE_CLOSE_OUTPUT = "pr_compute-close-output-channel";
    @Output(PR_COMPUTE_CLOSE_OUTPUT)
    MessageChannel computeCloseOutput();

}
