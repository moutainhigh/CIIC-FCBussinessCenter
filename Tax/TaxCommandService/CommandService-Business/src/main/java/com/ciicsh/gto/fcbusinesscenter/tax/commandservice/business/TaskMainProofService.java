package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForMainProof;

/**
 * @author yuantongqing
 * created 2017/12/08
 */
public interface TaskMainProofService {

    /**
     * 条件查询完税凭证总任务
     * @param requestForProof
     * @return
     */
    ResponseForMainProof queryTaskMainProofByRes(RequestForProof requestForProof);

    /**
     * 新增完税凭证主任务和完税凭证子任务
     * @param taskMainProofPO
     * @param taskSubProofPO
     * @return
     */
    Boolean addTaskProof(TaskMainProofPO taskMainProofPO, TaskSubProofPO taskSubProofPO);

    /**
     * 修改（即：提交）完税凭证状态
     * @param requestForProof
     * @return
     */
    Boolean updateTaskProofByRes(RequestForProof requestForProof);

    /**
     * 将完税凭证任务置为失效
     * @param requestForProof
     * @return
     */
    Boolean invalidTaskProof(RequestForProof requestForProof);

}

