package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wuhua
 */
public interface TaskSubSupplierMapper extends BaseMapper<TaskSubSupplierPO> {

    /**
     * 根据合并的ID拼接的in条件查询合并前的子任务ID
     *
     * @param sbCombinedParams
     * @return
     */
    List<Long> querySubSupplierIdsByMergeIds(@Param("sbCombinedParams") String sbCombinedParams);

    /**
     * 修改供应商合并任务ID为失效
     *
     * @param combinedId
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateSupplierByCombinedId(@Param("combinedId") Long combinedId, @Param("modifiedBy") String modifiedBy, @Param("modifiedTime") LocalDateTime modifiedTime);

    /**
     * 修改合并前供应商子任务为有效状态
     *
     * @param combinedId
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateSupplierToActiveById(@Param("combinedId") Long combinedId, @Param("modifiedBy") String modifiedBy, @Param("modifiedTime") LocalDateTime modifiedTime);
}