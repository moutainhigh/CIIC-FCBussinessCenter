package com.ciicsh.gto.salarymanagementcommandservice.util.messageBus;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@EnableBinding(value = WebSocketSink.class)
@Component
public class WebSocketReceiver {

    private final static Logger logger = LoggerFactory.getLogger(WebSocketReceiver.class);

    @Autowired
    private SimpMessagingTemplate template;

    @StreamListener(WebSocketSink.COMPUTE_STATUS_INPUT)
    public void receiveComputeStatus(ComputeMsg message){
        logger.info("获取计算状态结果: " + message.toString());
        String batchCode = message.getBatchCode();
        batchCode = batchCode + "-" + message.getOptID();
        int status = message.getComputeStatus();
        String dest = "/compute/status/" + batchCode; // 浏览器订阅的topic
        template.convertAndSend(dest,status);         // 转发消息给客户端
    }

}
