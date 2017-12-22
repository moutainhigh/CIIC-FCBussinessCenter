package com.ciicsh.caldispatchjob.compute.messageBus;

import com.ciicsh.caldispatchjob.compute.service.EmpGroupServiceImpl;
import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollEmpGroup;
import com.ciicsh.gto.salarymanagement.entity.message.PayrollMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by bill on 17/10/21.
 */
@EnableBinding(value = PayrollSink.class)
@Component
public class PayrollReceiver {

    private final static Logger logger = LoggerFactory.getLogger(PayrollReceiver.class);

    @Autowired
    private EmpGroupServiceImpl empGroupService;

    @StreamListener(PayrollSink.INPUT)
    public void receive(PayrollMsg message){
        logger.info("received from message: " + message.toString());
    }

    @StreamListener(PayrollSink.EMP_GROUP_INPUT)
    public void receive(PayrollEmpGroup message){
        //logger.info("received employee group from message: " + message.getIds().size());
        processMongodb(message);

    }

    private void processMongodb(PayrollEmpGroup message){
        List<String> empIds = message.getIds();
        List<String> groupIds = message.getEmpGroupIds();

        logger.info("Opt Operation Type :" + message.getOperateType());

        if(message.getOperateType() == OperateTypeEnum.ADD.getValue()){
            String empGroupId = String.valueOf(groupIds.toArray()[0]);
            logger.info("ADD Opt");
            logger.info("emp group id:" + empGroupId);
            empGroupService.batchInsertGroupEmployees(empGroupId,empIds);
        }
        else if(message.getOperateType() == OperateTypeEnum.DELETE.getValue()){
            logger.info("DELETE Opt");
            empGroupService.batchDelGroupEmployees(groupIds,empIds);
        }

    }
}