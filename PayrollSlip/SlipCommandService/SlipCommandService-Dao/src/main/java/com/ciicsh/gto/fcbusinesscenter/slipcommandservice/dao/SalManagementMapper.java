package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.SalManagementPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 管理方 Mapper 接口
 *
 * @author taka
 * @since 2018-02-05
 */
public interface SalManagementMapper extends BaseMapper<SalManagementPO> {

    int total(Map<String, Object> params);

    List<SalManagementPO> list(Map<String, Object> params);

    SalManagementPO get(Map<String, Object> params);

    SalManagementPO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);
}
