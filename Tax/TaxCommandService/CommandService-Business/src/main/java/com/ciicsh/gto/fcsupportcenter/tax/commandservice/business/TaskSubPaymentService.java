package com.ciicsh.gto.fcsupportcenter.tax.commandservice.business;

import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubPaymentPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.payment.RequestForSubPayment;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.payment.ResponseForSubPayment;

/**
 * @author yuantongqing on 2018/01/02
 */
public interface TaskSubPaymentService {

    /**
     * 条件查询缴纳子任务
     * @param requestForSubPayment
     * @return
     */
    ResponseForSubPayment querySubPayment(RequestForSubPayment requestForSubPayment);

    /**
     * 批量完成缴纳子任务
     * @param requestForSubPayment
     * @return
     */
    Boolean completeTaskSubPayment(RequestForSubPayment requestForSubPayment);

    /**
     * 批量退回缴纳子任务
     * @param requestForSubPayment
     * @return
     */
    Boolean rejectTaskSubPayment(RequestForSubPayment requestForSubPayment);


    /**
     * 根据缴纳子任务ID查询缴纳任务信息
     * @param subPaymentId
     * @return
     */
    TaskSubPaymentPO querySubPaymentById(Long subPaymentId);
}
