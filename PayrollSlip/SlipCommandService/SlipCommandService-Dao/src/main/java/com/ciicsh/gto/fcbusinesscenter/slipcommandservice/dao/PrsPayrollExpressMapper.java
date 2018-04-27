package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollExpressPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 工资单邮寄快递信息 Mapper 接口
 *
 * @author taka
 * @since 2018-02-24
 */
@Repository
public interface PrsPayrollExpressMapper extends BaseMapper<PrsPayrollExpressPO> {

    int total(Map<String, Object> params);

    List<PrsPayrollExpressPO> list(Map<String, Object> params);

    PrsPayrollExpressPO get(Map<String, Object> params);

    PrsPayrollExpressPO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);
}
