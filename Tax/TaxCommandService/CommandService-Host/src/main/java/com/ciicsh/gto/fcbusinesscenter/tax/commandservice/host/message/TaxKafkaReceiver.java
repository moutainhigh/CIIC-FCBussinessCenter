package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.MongodbService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayApplyPayStatusDTO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCreateMsgDTO;
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

    @Autowired
    private MongodbService mongodbService;

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

    /**
     * 计算引擎关账通知
     * @param closingMsg
     */
    @StreamListener(TaxSink.PR_COMPUTE_CLOSE_OUTPUT_CHANNEL)
    public void clMessage(ClosingMsg closingMsg) {
        try {
            if(closingMsg!=null && closingMsg.getBatchCode()!=null){
                mongodbService.acquireBatch(closingMsg);
            }
        } catch (Exception e) {
            logger.error("关账消息处理异常!",e);
        }
    }

    /**
     * 计算引擎取消关账通知
     * @param cancelClosingMsg
     */
    @StreamListener(TaxSink.PR_COMPUTE_UNCLOSE_OUTPUT_CHANNEL)
    public void unclMessage(CancelClosingMsg cancelClosingMsg) {
        try {
            mongodbService.cancelBatch(cancelClosingMsg);
        } catch (Exception e) {
            logger.error("计算引擎取消关账信息异常!",e);
        }
    }

    /**
     * 工作流通知
     * @param
     */
    @StreamListener(TaxSink.WORKFLOW_CHANNEL)
    public void wfMessage(TaskCreateMsgDTO taskCreateMsgDTO) {

        System.out.println("#########################");

        if(taskCreateMsgDTO!=null){
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+taskCreateMsgDTO.toString());
        }

        try {


        } catch (Exception e) {
            logger.error("工作流通知信息异常!",e);
        }
    }
}
