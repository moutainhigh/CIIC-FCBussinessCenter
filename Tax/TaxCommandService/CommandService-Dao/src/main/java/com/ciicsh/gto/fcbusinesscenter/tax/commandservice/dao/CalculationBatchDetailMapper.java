package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;

import java.util.List;

/**
 * <p>
  * 计算批次明细 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-19
 */
public interface CalculationBatchDetailMapper extends BaseMapper<CalculationBatchDetailPO> {

    /**
     * 条件查询计算批次明细(分页)
     * @param page
     * @param calculationBatchDetailBO
     * @return
     */
    List<CalculationBatchDetailBO> queryTaxBatchDetailByRes(Pagination page, CalculationBatchDetailBO calculationBatchDetailBO);
}