package com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus;

import com.ciicsh.gto.salarymanagement.entity.message.ComputeMsg;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author
 */
@EnableBinding(value = PayCalSink.class)
@Component
public class PayrollReceiver {

    private final static Logger logger = LoggerFactory.getLogger(PayrollReceiver.class);

    @Autowired
    private PrGroupTemplateService prGroupTemplateService;

    @Autowired
    private PrGroupService prGroupService;

    @StreamListener(PayCalSink.MANAGEMENT_INPUT)
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
