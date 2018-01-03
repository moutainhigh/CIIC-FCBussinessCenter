package com.ciicsh.gto.fcsupportcenter.tax.commandservice.business;

import com.ciicsh.gto.fcsupportcenter.tax.entity.request.payment.RequestForSubPaymentDetail;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.payment.ResponseForSubPaymentDetail;

/**
 * @author yuantongqing
 * on create 2018/1/3
 */
public interface TaskSubPaymentDetailService {

    /**
     * 查询缴纳明细
     * @param requestForSubPaymentDetail
     * @return
     */
    ResponseForSubPaymentDetail querySubPaymentDetailsByParams(RequestForSubPaymentDetail requestForSubPaymentDetail);
}
