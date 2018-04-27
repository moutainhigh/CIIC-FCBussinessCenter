package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPrintRecordPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 工资单任务单打印记录 Mapper 接口
 *
 * @author taka
 * @since 2018-02-24
 */
@Repository
public interface PrsPayrollPrintRecordMapper extends BaseMapper<PrsPayrollPrintRecordPO> {

    int total(Map<String, Object> params);

    List<PrsPayrollPrintRecordPO> list(Map<String, Object> params);

    PrsPayrollPrintRecordPO get(Map<String, Object> params);

    PrsPayrollPrintRecordPO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);
}
