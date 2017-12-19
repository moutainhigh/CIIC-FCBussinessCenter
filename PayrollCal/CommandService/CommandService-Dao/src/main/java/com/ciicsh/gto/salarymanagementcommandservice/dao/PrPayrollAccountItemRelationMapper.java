package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 薪资账套薪资项名表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-19
 */
@Mapper
@Component
public interface PrPayrollAccountItemRelationMapper extends BaseMapper<PrPayrollAccountItemRelationPO> {

}