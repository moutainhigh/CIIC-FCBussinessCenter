package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 配置表，薪资项明细表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Component
public interface PrPayrollItemMapper extends BaseMapper<PrPayrollItemPO> {

}