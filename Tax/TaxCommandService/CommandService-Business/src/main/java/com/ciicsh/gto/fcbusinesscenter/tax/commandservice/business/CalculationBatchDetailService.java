package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatchDetail;
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
     * @param requestForEmployees
     * @return
     */
    ResponseForCalBatchDetail queryCalculationBatchDetails(RequestForEmployees requestForEmployees);

    /**
     * 条件查询计算批次明细
     * @param requestForCalBatchDetail
     * @return
     */
    ResponseForCalBatchDetail queryTaxBatchDetailByRes(RequestForCalBatchDetail requestForCalBatchDetail);

    /**
     * 批量恢复计算批次明细
     * @param ids
     */
    void queryCalculationBatchDetail(String[] ids);
}
