package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoneyDetail;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
public interface TaskSubMoneyDetailService {

    /**
     * 条件查询划款明细
     * @param requestForSubMoneyDetail
     * @return
     */
    ResponseForSubMoneyDetail querySubMoneyDetailsByParams(RequestForSubMoneyDetail requestForSubMoneyDetail);
}
