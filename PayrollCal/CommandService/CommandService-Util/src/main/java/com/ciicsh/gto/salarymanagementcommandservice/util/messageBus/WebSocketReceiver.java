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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author NeoJiang
 */
@EnableBinding(value = PayCalSink.class)
@Component
public class PayrollReceiver {

    private final static Logger logger = LoggerFactory.getLogger(PayrollReceiver.class);

    @Autowired
    private SimpMessagingTemplate template;


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

    @StreamListener(PayCalSink.COMPUTE_STATUS_INPUT)
    public void receiveComputeStatus(ComputeMsg message){
        logger.info("获取计算状态结果: " + message.toString());
        String batchCode = message.getBatchCode();
        int status = message.getComputeStatus();
        String dest = "/compute/status/" + batchCode; // 浏览器订阅的topic
        template.convertAndSend(dest,status);         // 转发消息给客户端
    }

}
