package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
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

    List<PrCustSubBatchPO> selectSubBatchList(@Param("code") String code, @Param("status") Integer status);

    /**
     * delete batch by code
     * @param codes
     * @return
     */
    Integer deleteBatchByCodes(@Param("codes") List<String> codes);

}