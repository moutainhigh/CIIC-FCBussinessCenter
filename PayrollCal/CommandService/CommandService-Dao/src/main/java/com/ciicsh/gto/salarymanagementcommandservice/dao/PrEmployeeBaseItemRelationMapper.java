package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeBaseItemRelationPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 固定项表。记录固定项的名字和值，和雇员雇员ID关联和批次无关。 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Mapper
@Component
public interface PrEmployeeBaseItemRelationMapper extends BaseMapper<PrEmployeeBaseItemRelationPO> {

}