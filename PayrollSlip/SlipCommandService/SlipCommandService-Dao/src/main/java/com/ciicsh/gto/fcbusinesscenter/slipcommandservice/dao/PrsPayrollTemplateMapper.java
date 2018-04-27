package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollTemplatePO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 工资单模板 Mapper 接口
 *
 * @author taka
 * @since 2018-01-29
 */
@Repository
public interface PrsPayrollTemplateMapper extends BaseMapper<PrsPayrollTemplatePO> {

    int total(Map<String, Object> params);

    List<PrsPayrollTemplatePO> list(Map<String, Object> params);

    PrsPayrollTemplatePO get(Map<String, Object> params);

    PrsPayrollTemplatePO last();

    void insert(Map<String, Object> params);

    void update(Map<String, Object> params);
}
