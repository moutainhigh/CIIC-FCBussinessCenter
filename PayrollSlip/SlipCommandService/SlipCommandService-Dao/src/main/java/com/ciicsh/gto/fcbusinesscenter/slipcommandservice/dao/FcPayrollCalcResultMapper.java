package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.FcPayrollCalcResultPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 薪资计算批次结果表(雇员维度) Mapper 接口
 *
 * @author taka
 * @since 2018-02-09
 */
public interface FcPayrollCalcResultMapper extends BaseMapper<FcPayrollCalcResultPO> {

    int total(Map<String, Object> params);

    List<FcPayrollCalcResultPO> list(Map<String, Object> params);

    FcPayrollCalcResultPO get(Map<String, Object> params);

    FcPayrollCalcResultPO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);

    List<FcPayrollCalcResultPO> listBatchIds(Map<String, Object> params);

    List<FcPayrollCalcResultPO> listPayrollTypes(Map<String, Object> params);
}
