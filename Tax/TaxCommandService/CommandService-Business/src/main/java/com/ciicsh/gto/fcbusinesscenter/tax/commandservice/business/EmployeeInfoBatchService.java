package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;

import java.util.List;

/**
 * @author yuantongqing on 2018-05-22
 */
public interface EmployeeInfoBatchService {
    /**
     * 根据划款明细计算批次明细ID查询雇员个税信息
     * @param batchIds
     * @return
     */
    List<EmployeeInfoBatchPO> queryEmployeeInfoBatchesByBatchIds(List batchIds);
}
