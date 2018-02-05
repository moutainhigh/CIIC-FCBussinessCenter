package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * @author yuantongqing on 2018/1/31
 */
@Service
public interface TaxSink {
    String TAX_INPUT_TEST = "tax_input_test";

    /**
     * kafka接收测试
     * @return
     */
    @Input(TAX_INPUT_TEST)
    MessageChannel taxInputTest();
}