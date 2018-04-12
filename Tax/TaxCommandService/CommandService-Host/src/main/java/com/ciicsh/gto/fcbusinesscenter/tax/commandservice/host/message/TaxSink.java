package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * @author yuantongqing on 2018/1/31
 */
@Service
public interface TaxSink {

    /**
     * 结算中心划款topic
     */
    String PAY_APPLY_PAY_STATUS_STREAM = "pay-apply-pay-status-stream";


    /**
     * 监听结算中心划款消息
     * @return
     */
    @Input(PAY_APPLY_PAY_STATUS_STREAM)
    MessageChannel moneyMessage();

}
