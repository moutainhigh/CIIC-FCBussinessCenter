package com.ciicsh.gt1.fcbusinesscenter.compute.messageBus;

import com.ciicsh.caldispatchjob.compute.Cal.ComputeServiceImpl;
import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 18/4/19.
 */
@EnableBinding(value = ComputeSink.class)
@Component
public class ComputeReceiver {

    private final static Logger logger = LoggerFactory.getLogger(ComputeReceiver.class);

    @Autowired
    private ComputeServiceImpl computeService;

    @StreamListener(ComputeSink.PR_COMPUTE_INPUT)
    public void receive(ComputeMsg computeMsg){
        logger.info("获取页面计算运行消息： " + computeMsg);
        if(computeMsg.getBatchType() > 0) {
            processPayrollCompute(computeMsg.getBatchCode(), computeMsg.getBatchType());
        }
    }

    /**
     * 接收薪资计算消息
     * @param batchCode
     */
    private void processPayrollCompute(String batchCode,int batchType){
        computeService.processCompute(batchCode,batchType);
    }
}
