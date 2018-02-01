package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author yuantongqing on 2018/1/31
 */
@EnableBinding(value = TaxSource.class)
@Component
public class TaxSender {
    private final static Logger logger = LoggerFactory.getLogger(TaxSender.class);

    @Autowired
    private TaxSource taxSource;

    public void taxOutputTest(String  str) {
        taxSource.taxOutputTest().send(MessageBuilder.withPayload(str).build());
    }
}
