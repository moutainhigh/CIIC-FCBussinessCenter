package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoneyDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoneyDetail;

import java.util.List;

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

    /**
     * 根据划款子任务ID查询划款任务明细
     * @param subMoneyId
     * @return
     */
    List<TaskSubMoneyDetailPO> querySubMonetDetailsBySubMoneyId(Long subMoneyId);

    /**
     * 根据计算批次明细ID集合查询划款明细集合
     * @param taskSubMoneyDetailIdsList
     * @return
     */
    List<TaskSubMoneyDetailPO> querySubMonetDetailsByBatchDetailIds(List<Long> taskSubMoneyDetailIdsList);
}
