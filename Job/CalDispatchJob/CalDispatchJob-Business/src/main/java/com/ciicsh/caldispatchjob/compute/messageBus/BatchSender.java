package com.ciicsh.caldispatchjob.compute.messageBus;

import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 18/5/23.
 */
@EnableBinding(value = BatchSource.class)
@Component
public class BatchSender {

    private final Logger logger = LoggerFactory.getLogger(BatchSender.class);

    @Autowired
    private BatchSource batchSource;

    /**
     * 发送薪资关帐
     * @param closingMsg
     */
    public void SendComputeClose(ClosingMsg closingMsg ){
        try {
            logger.info("发送关帐消息到其他业务中心");
            batchSource.computeCloseOutput().send(MessageBuilder.withPayload(closingMsg).build());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }
}
