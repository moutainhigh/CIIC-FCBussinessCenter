package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 正常批次主表 Mapper 接口
 * </p>
 *
 * @author Bill
 * @since 2017-12-05
 */
@Component
public interface PrNormalBatchMapper extends BaseMapper<PrNormalBatchPO> {

    int insertNormalBatch(PrNormalBatchPO normalBatchPO);

    List<PrCustBatchPO> selectBatchListByUseLike(PrCustBatchPO prCustBatchPO);

    int updateNormalBatch(PrNormalBatchPO normalBatchPO);

    List<PrCustSubBatchPO> selectSubBatchList(@Param("code") String code);

    /**
     * delete batch by code
     *
     * @param codes
     * @return
     */
    Integer deleteBatchByCodes(@Param("codes") List<String> codes);

    int auditBatch(@Param("code") String batchCode,
                   @Param("comments") String comments,
                   @Param("status") int status,
                   @Param("modifiedBy") String modifiedBy,
                   @Param("result") String result);


    int updateHasAdvance(@Param("codes") List<String> batchCodes,
                         @Param("hasAdvance") int hasAdvance,
                         @Param("modifiedBy") String modifiedBy);

    int updateHasMoney(@Param("codes") List<String> batchCodes,
                        @Param("hasMoney") boolean hasMoney,
                        @Param("modifiedBy") String modifiedBy);

    List<PrNormalBatchPO> selectAllBatchCodesByManagementId(@Param("managementId") String managementId);

    List<String> getBatchIdListByManagementId(@Param("managementId") String managementId);

    List<PrNormalBatchPO> getHistoryBatchInfoList(@Param("mgrIds") List<String> mgrIds);

    List<PrNormalBatchPO> selectAllBatchCodesByManagementId(@Param("managementId") String managementId,
                                                            @Param("batchCode") String batchCode);
    List<PrPayrollItemPO> selectBatchPayrollSchema(@Param("batchCode") String batchCode);

    PrCustBatchPO getCustBatchInfo(@Param("batchCode") String batchCode);
}