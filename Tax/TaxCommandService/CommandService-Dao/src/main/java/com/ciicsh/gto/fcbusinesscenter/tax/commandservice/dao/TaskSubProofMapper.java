package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubDeclareDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 完税凭证子任务 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-07
 */
public interface TaskSubProofMapper extends BaseMapper<TaskSubProofPO> {

    /**
     * 根据主键ID重新计算子任务总人数
     * @param id
     */
    void updateSubHeadcountById(@Param("id") Long id);

    /**
     * 查询完税凭证子任务(分页)
     * @param page
     * @param taskSubProofBO
     * @return
     */
    List<TaskSubProofBO> querySubProofInfoByTaskType(Pagination page, TaskSubProofBO taskSubProofBO);

    /**
     * 根据合并的ID拼接的in条件查询合并前的子任务ID
     * @param sbCombinedParams
     * @return
     */
    List<Long> querySubIdsByCombinedIds(@Param("sbCombinedParams") String sbCombinedParams);

    /**
     * 根据主键ID查询完税凭证子任务详细信息
     * @param subProofId
     * @return
     */
    TaskSubProofBO queryApplyDetailsBySubId(@Param("subProofId") Long subProofId);

    /**
     * 申报详细信息
     * @param taskSubDeclareId
     * @return
     */
    List<TaskSubDeclareDetailBO> querySubDeclareDetailsForProof(@Param("taskSubDeclareId") Long taskSubDeclareId);

}