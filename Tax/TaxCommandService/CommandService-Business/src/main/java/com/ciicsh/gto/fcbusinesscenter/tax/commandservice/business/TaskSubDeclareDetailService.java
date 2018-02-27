package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForSubDeclareDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForSubDeclareDetail;

import java.util.List;

/**
 * @author wuhua
 */
public interface TaskSubDeclareDetailService {
    /**
     * 根据申报子任务ID查询申报明细
     *
     * @param subDeclareId
     * @return
     */
    List<TaskSubDeclareDetailPO> querySubDeclareDetailList(Long subDeclareId);

    /**
     * 查询申报明细
     *
     * @param requestForSubDeclareDetail
     * @return
     */
    ResponseForSubDeclareDetail querySubDeclareDetailsByParams(RequestForSubDeclareDetail requestForSubDeclareDetail);

}

