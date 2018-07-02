package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPayment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author yuantongqing on 2018/01/02
 */
@Service
public interface TaskSubPaymentService {

    /**
     * 条件查询缴纳子任务
     *
     * @param requestForSubPayment
     * @return
     */
    ResponseForSubPayment querySubPayment(RequestForSubPayment requestForSubPayment);

    /**
     * 批量完成缴纳子任务
     *
     * @param requestForSubPayment
     */
    void completeTaskSubPayment(RequestForSubPayment requestForSubPayment);

    /**
     * 批量退回缴纳子任务
     *
     * @param requestForSubPayment
     */
    void rejectTaskSubPayment(RequestForSubPayment requestForSubPayment);


    /**
     * 根据缴纳子任务ID查询缴纳任务信息
     *
     * @param subPaymentId
     * @return
     */
    TaskSubPaymentPO querySubPaymentById(long subPaymentId);

    /**
     * 更新划款滞纳金和罚金
     * @param subPayId
     * @param overdue
     * @param fine
     */
    void updateTaskSubPayOverdueAndFine(Long subPayId, BigDecimal overdue, BigDecimal fine);
}
