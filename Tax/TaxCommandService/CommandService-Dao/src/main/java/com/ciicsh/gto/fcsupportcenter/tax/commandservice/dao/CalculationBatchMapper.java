package com.ciicsh.gto.fcsupportcenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.CalculationBatchBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.CalculationBatchPO;

import java.util.List;

/**
 * @author wuhua
 */
public interface CalculationBatchMapper extends BaseMapper<CalculationBatchPO> {

    /**
     * 查询计算批次列表
     * @param calculationBatchBO
     * @return
     */
    List<CalculationBatchPO> queryCalculationBatchsByCondition(CalculationBatchBO calculationBatchBO);

}