package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayApplyPayStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TaskSubMoneyService taskSubMoneyService;

    /**
     * 监听结算中心划款返回信息
     * @param payApplyPayStatusDTO
     */
    @StreamListener(TaxSink.PAY_APPLY_PAY_STATUS_STREAM)
    public void moneyMessage(PayApplyPayStatusDTO payApplyPayStatusDTO) {
        try {
            //上海个税类型为6
            if (payApplyPayStatusDTO.getBusinessType() == 6) {
                long subMoneyId = payApplyPayStatusDTO.getBusinessPkId();
                int status = payApplyPayStatusDTO.getPayStatus();
                TaskSubMoneyBO taskSubMoneyBO = new TaskSubMoneyBO();
                taskSubMoneyBO.setId(subMoneyId);
                if (status == 9) {
                    logger.info(subMoneyId + "划款成功");
                    //修改
                    taskSubMoneyBO.setPayStatus("02");
                    try {
                        taskSubMoneyService.updateTaskSubMoneyById(taskSubMoneyBO);
                    } catch (Exception e) {
                        logger.error("根据结算中心返回划款信息,更新状态为成功失败!",e);
                    }
                }
                if(status == -1){
                    logger.info(subMoneyId + "划款驳回");
                    taskSubMoneyBO.setPayStatus("03");
                    try {
                        taskSubMoneyService.updateTaskSubMoneyById(taskSubMoneyBO);
                    } catch (Exception e) {
                        logger.error("根据结算中心返回划款信息,更新状态为失败失败!",e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("结算中心划款返回信息异常!",e);
        }
    }
}
