package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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

    int updateHasAdvance(@Param("codes") List<String> batchCodes,
                             @Param("hasAdvance") int hasAdvance,
                             @Param("modifiedBy") String modifiedBy);

    int updateHasMoney(@Param("codes") List<String> batchCodes,
                             @Param("hasMoney") boolean hasMoney,
                             @Param("modifiedBy") String modifiedBy);

    Integer deleteBatchByCodes(@Param("codes") List<String> codes);

    int auditBatch(@Param("code") String batchCode,
                   @Param("comments") String comments,
                   @Param("status") int status,
                   @Param("modifiedBy") String modifiedBy,
                   @Param("advancePeriod") String advancePeriod,
                   @Param("result") String result);

    int checkBackTraceBatch(@Param("originBatchCode") String originBatchCode);

    PrBackTrackingBatchPO getPrBackTrackingBatchPO(@Param("code") String batchCode);
}