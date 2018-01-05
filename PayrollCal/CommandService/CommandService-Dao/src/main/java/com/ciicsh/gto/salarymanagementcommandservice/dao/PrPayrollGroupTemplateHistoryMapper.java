package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplateHistoryPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 薪资组模板版本历史记录表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2018-01-04
 */
@Component
public interface PrPayrollGroupTemplateHistoryMapper extends BaseMapper<PrPayrollGroupTemplateHistoryPO> {

    PrPayrollGroupTemplateHistoryPO selectLastVersionByCode(@Param("code") String code);
}