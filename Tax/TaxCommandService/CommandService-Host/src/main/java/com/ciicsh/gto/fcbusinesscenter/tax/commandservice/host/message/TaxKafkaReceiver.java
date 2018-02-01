package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author yuantongqing on 2018/1/31
 */
@EnableBinding(value = TaxSink.class)
@Component
public class TaxKafkaReceiver {
    private final static Logger logger = LoggerFactory.getLogger(TaxKafkaReceiver.class);

    @StreamListener(TaxSink.TAX_INPUT_TEST)
    public void taxInputTest(EmployeeDTO employeeDTO) {
        logger.info("kafka接受信息:" + employeeDTO.toString());
    }
}
