package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 工资单 Mapper 接口
 *
 * @author taka
 * @since 2018-02-09
 */
public interface PrsPayrollMapper extends BaseMapper<PrsPayrollPO> {

    int total(Map<String, Object> params);

    List<PrsPayrollPO> list(Map<String, Object> params);

    PrsPayrollPO get(Map<String, Object> params);

    PrsPayrollPO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);
}
