package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wuhua
 */
public interface CalculationBatchMapper extends BaseMapper<CalculationBatchPO> {

    /**
     * 查询计算批次列表
     * @param calculationBatchBO
     * @return
     *//*
    List<CalculationBatchBO> queryCalculationBatchsByCondition(CalculationBatchBO calculationBatchBO);*/

    /**
     * 查询计算批次明细列表
     * @param page
     * @param batchIds
     * @return
     */
    List<CalculationBatchDetailBO> queryCalculationBatchDetails(Pagination page,@Param("batchIds")Long[] batchIds);
    /**
     * 查询计算批次明细列表
     * @param batchIds
     * @return
     */
    List<CalculationBatchDetailBO> queryCalculationBatchDetails(@Param("batchIds")Long[] batchIds);

}