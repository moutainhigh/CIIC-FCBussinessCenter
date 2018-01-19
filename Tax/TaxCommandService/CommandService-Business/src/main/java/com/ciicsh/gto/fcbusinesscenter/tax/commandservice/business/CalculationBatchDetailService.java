package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
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

    /**
     * 查询计算批次明细信息
     * @param
     * @return
     */
    ResponseForCalBatchDetail queryCalculationBatchDetails(RequestForEmployees requestForEmployees);
}
