package com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus;

import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupTemplateService;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.KafkaReceiver;
import com.ciicsh.gto.salarymanagementcommandservice.util.messageBus.PayrollSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author NeoJiang
 */
@EnableBinding(value = PayrollSink.class)
@Component
public class PayrollReceiver {

    private final static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    @Autowired
    private PrGroupTemplateService prGroupTemplateService;

    @Autowired
    private PrGroupService prGroupService;

    @StreamListener(PayrollSink.MANAGEMENT_INPUT)
    public void managementCreatedRecieveHandler(String managementId){
        logger.info("received from message: " + managementId);

        PrPayrollGroupTemplatePO param = new PrPayrollGroupTemplatePO();
        param.setActive(true);
        List<PrPayrollGroupTemplatePO> prPayrollGroupTemplatePOList
                = prGroupTemplateService.getList(param);
        prPayrollGroupTemplatePOList.forEach(i -> {
            PrPayrollGroupPO paramGroup = new PrPayrollGroupPO();
            paramGroup.setGroupName(managementId + "-" + i.getGroupTemplateName());
            paramGroup.setGroupTemplateCode(i.getGroupTemplateCode());
            paramGroup.setManagementId(managementId);
            paramGroup.setCreatedBy("sys");
            paramGroup.setModifiedBy("sys");
            prGroupService.addItem(paramGroup);
        });
    }

}
