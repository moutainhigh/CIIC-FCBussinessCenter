package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountItemRelationPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据薪资账套Code 删除薪资账套薪资项关系数据
     * @param accountSetCode 薪资账套Code
     * @return 返回影响的行数
     */
    Integer delAccountItemRelationByAccountCode(@Param("accountSetCode") String accountSetCode);
}