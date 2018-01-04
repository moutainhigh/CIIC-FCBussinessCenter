package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPaymentDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPaymentDetail;
import org.springframework.stereotype.Service;

/**
 * @author yuantongqing
 * on create 2018/1/3
 */
@Service
public interface TaskSubPaymentDetailService {

    /**
     * 查询缴纳明细
     * @param requestForSubPaymentDetail
     * @return
     */
    ResponseForSubPaymentDetail querySubPaymentDetailsByParams(RequestForSubPaymentDetail requestForSubPaymentDetail);
}
