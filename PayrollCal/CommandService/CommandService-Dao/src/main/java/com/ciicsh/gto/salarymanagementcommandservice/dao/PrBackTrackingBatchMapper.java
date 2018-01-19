package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
  * 回溯批次主表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Component
public interface PrBackTrackingBatchMapper extends BaseMapper<PrBackTrackingBatchPO> {

    int updateHasAdvance(@Param("code") String batchCode,
                             @Param("hasAdvance") boolean hasAdvance,
                             @Param("modifiedBy") String modifiedBy);

    int updateHasMoneny(@Param("code") String batchCode,
                             @Param("hasMoney") boolean hasMoney,
                             @Param("modifiedBy") String modifiedBy);
}