package com.ciicsh.gto.fcbusinesscenter.tax.queryservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;

/**
 * @author yuantongqing on 2017/12/19
 */
public interface CalculationBatchDetailService {

    /**
     * 查询申报记录信息
     * @param requestForProof
     * @return
     */
    ResponseForBatchDetail queryCalculationBatchDetail(RequestForProof requestForProof);
}
