package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * @author yuantongqing on 2018/1/31
 */
@Service
public interface TaxSource {
    String TAX_OUTPUT_TEST = "tax_output_test";

    /**
     * kafka测试
     * @return
     */
    @Output(TAX_OUTPUT_TEST)
    MessageChannel taxOutputTest();
}
