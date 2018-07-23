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

    /**
     * 根据有合并明细的申报ID查询未确认的数目
     *
     * @param hasCombinedDeclareIds
     * @return
     */
    int selectCount(String[] hasCombinedDeclareIds);

    /**
     * 根据申报明细ID查询申报明细合并前数据信息
     * @param subDeclareDetailId
     * @return
     */
    List<TaskSubDeclareDetailPO> querySubDeclareDetailListBeforeMergeByMergeId(Long subDeclareDetailId);

}

