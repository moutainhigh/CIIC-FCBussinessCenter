package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsSubTaskPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 工资单子任务单主表 Mapper 接口
 *
 * @author taka
 * @since 2018-02-11
 */
public interface PrsSubTaskMapper extends BaseMapper<PrsSubTaskPO> {

    int total(Map<String, Object> params);

    List<PrsSubTaskPO> list(Map<String, Object> params);

    PrsSubTaskPO get(Map<String, Object> params);

    PrsSubTaskPO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);

    void updateByMainTaskId(Map<String, Object> params);
}
