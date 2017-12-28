package com.ciicsh.gto.fcsupportcenter.tax.commandservice.business;

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
     *
     * @param taskMainProofId
     * @return
     */
    List<TaskSubProofPO> queryTaskSubProofByMainId(Long taskMainProofId);

    /**
     * 根据请求参数查询完税子任务信息
     *
     * @param requestForProof
     * @return
     */
    ResponseForSubProof queryTaskSubProofByRes(RequestForProof requestForProof);

    /**
     * 根据子任务ID复制相关数据
     *
     * @param taskSubProofId
     * @return
     */
    Boolean copyProofInfoBySubId(Long taskSubProofId);

    /**
     * 多表查询完税凭证子任务
     *
     * @param requestForProof
     * @return
     */
    ResponseForSubProof querySubProofInfoByTaskType(RequestForProof requestForProof);

    /**
     * 合并完税凭证子任务
     *
     * @param requestForProof
     * @return
     */
    Boolean combineTaskProofByRes(RequestForProof requestForProof);

    /**
     * 拆分任务
     *
     * @param requestForProof
     * @return
     */
    Boolean splitTaskProofByRes(RequestForProof requestForProof);

    /**
     * 批量完成完税凭证子任务
     *
     * @param requestForProof
     * @return
     */
    Boolean completeTaskProofByRes(RequestForProof requestForProof);

    /**
     * 批量退回完税凭证子任务
     * @param requestForProof
     * @return
     */
    Boolean rejectTaskProofByRes(RequestForProof requestForProof);

    /**
     * 批量失效完税凭证子任务
     * @param requestForProof
     * @return
     */
    Boolean invalidTaskProofByRes(RequestForProof requestForProof);

}
