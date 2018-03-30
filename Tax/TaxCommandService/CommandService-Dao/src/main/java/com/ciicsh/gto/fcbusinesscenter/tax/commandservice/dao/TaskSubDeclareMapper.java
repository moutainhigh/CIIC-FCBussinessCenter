package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wuhua
 */
public interface TaskSubDeclareMapper extends BaseMapper<TaskSubDeclarePO> {

    /**
     * 根据合并的ID拼接的in条件查询合并前的子任务ID
     * @param sbCombinedParams
     * @return
     */
    List<Long> querySubDeclareIdsByMergeIds(@Param("sbCombinedParams") String sbCombinedParams);

    /**
     * 修改申报子任务合并ID为数组ID的任务状态
     *
     * @param subDeclareIds
     * @param status
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateBeforeMergeDeclareStatus(@Param("subDeclareIds")String[] subDeclareIds,@Param("status") String status ,@Param("modifiedBy") String modifiedBy, @Param("modifiedTime") LocalDateTime modifiedTime);

    /**
     * 修改申报合并任务ID为失效
     * @param combinedId
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateDeclareByCombinedId(@Param("combinedId")Long combinedId,@Param("modifiedBy") String modifiedBy, @Param("modifiedTime") LocalDateTime modifiedTime);

    /**
     * 修改合并前申报子任务为有效状态
     * @param combinedId
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateDeclareToActiveById(@Param("combinedId")Long combinedId,@Param("modifiedBy") String modifiedBy, @Param("modifiedTime") LocalDateTime modifiedTime);

    /**
     * 修改合并前申报明细为有效状态
     * @param combinedId
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateDeclareDetailToActiveById(@Param("combinedId")Long combinedId,@Param("modifiedBy") String modifiedBy, @Param("modifiedTime") LocalDateTime modifiedTime);

    /**
     * 修改合并申报明细为失效状态
     * @param combinedId
     * @param modifiedBy
     * @param modifiedTime
     */
    void updateDeclareDetailById(@Param("combinedId")Long combinedId,@Param("modifiedBy") String modifiedBy, @Param("modifiedTime") LocalDateTime modifiedTime);
}