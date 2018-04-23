package com.ciicsh.caldispatchjob.compute.Cal;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 18/4/19.
 */
@Service
public interface ComputeSource {
    String PR_COMPUTE_OUTPUT = "pr_compute-status-output-channel";

    @Output(PR_COMPUTE_OUTPUT)
    MessageChannel computeStatusOutput();
}
