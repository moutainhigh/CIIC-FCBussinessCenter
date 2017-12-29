package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupHistoryPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 薪资组版本历史记录表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-28
 */
@Component
public interface PrPayrollGroupHistoryMapper extends BaseMapper<PrPayrollGroupHistoryPO> {

    PrPayrollGroupHistoryPO selectLastVersionByCode(@Param("code") String code);

}