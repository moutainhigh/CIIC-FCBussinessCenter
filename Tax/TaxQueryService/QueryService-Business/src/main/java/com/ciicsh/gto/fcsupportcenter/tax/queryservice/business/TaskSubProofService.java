package com.ciicsh.gto.fcsupportcenter.tax.queryservice.business;

import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForSubProof;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/12
 */
public interface TaskSubProofService {

    /**
     * 根据完税主任务ID查询其下完税子任务
     * @param taskMainProofId
     * @return
     */
    List<TaskSubProofPO> queryTaskSubProofByMainId(Long taskMainProofId);

    /**
     * 根据请求参数查询完税子任务信息
     * @param requestForProof
     * @return
     */
    ResponseForSubProof queryTaskSubProofByRes(RequestForProof requestForProof);

}
