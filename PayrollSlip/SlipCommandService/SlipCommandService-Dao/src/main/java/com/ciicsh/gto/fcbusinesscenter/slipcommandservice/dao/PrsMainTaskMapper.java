package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsMainTaskPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 工资单任务单主表 Mapper 接口
 *
 * @author taka
 * @since 2018-02-11
 */
@Repository
public interface PrsMainTaskMapper extends BaseMapper<PrsMainTaskPO> {

    int total(Map<String, Object> params);

    List<PrsMainTaskPO> list(Map<String, Object> params);

    PrsMainTaskPO get(Map<String, Object> params);

    PrsMainTaskPO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);
}
