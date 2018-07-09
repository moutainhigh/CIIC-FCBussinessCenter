package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import org.springframework.cloud.stream.annotation.Input;

public interface SlipMessageChannel {
    @Input("pr_compute-close-output-channel")
    org.springframework.messaging.MessageChannel input();
}
