package com.ciicsh.gto.fcsupportcenter.tax.queryservice.business;

import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForSubDetail;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForSubDetail;

/**
 * @author yuantongqing on 2017/12/14
 */
public interface TaskSubProofDetailService{

    /**
     * 查询完税申请明细
     * @param requestForProof
     * @return
     */
    ResponseForSubDetail queryTaskSubProofDetail(RequestForProof requestForProof);

    /**
     * 批量保存完税凭证申请明细
     * @param requestForSubDetail
     * @return
     */
    Boolean saveSubProofDetail(RequestForSubDetail requestForSubDetail);

}
