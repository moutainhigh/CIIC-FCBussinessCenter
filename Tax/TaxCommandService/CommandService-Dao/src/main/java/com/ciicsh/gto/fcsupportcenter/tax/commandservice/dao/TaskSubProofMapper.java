package com.ciicsh.gto.fcsupportcenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofPO;
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
     * 新增完税凭证子任务
     * @param taskSubProofPO
     * @return
     */
    int addTaskSubProof(TaskSubProofPO taskSubProofPO);


    /**
     * 根据子任务主键ID数组修改（即：提交）完税凭证子任务状态
     * @param subProofIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateSubTaskProof(@Param("subProofIds") String[] subProofIds, @Param("status") String status, @Param("modifiedBy") String modifiedBy);

    /**
     * 根据主任务ID数组修改（即：提交）完税凭证子任务状态
     * @param mainProofIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateSubTaskProofByMainIds(@Param("mainProofIds") String[] mainProofIds, @Param("status") String status, @Param("modifiedBy") String modifiedBy);


    /**
     * 根据子任务主键ID数组将完税凭证子任务置为失效
     * @param subProofIds
     * @param modifiedBy
     * @return
     */
    Boolean invalidSubTaskProofByIds(@Param("subProofIds") String[] subProofIds, @Param("modifiedBy") String modifiedBy);

    /**
     * 根据主任务ID数组将完税凭证子任务置为失效
     * @param mainProofIds
     * @param modifiedBy
     * @return
     */
    Boolean invalidSubTaskProofByMainIds(@Param("mainProofIds") String[] mainProofIds, @Param("modifiedBy") String modifiedBy);

    /**
     * 根据主任务ID，查询其下所有子任务ID的相关申报账户
     * @param mainId
     * @return
     */
    List<TaskSubProofPO> selectSubTaskMapByMainId(Long mainId);

    /**
     * 根据主键ID重新计算子任务总人数
     * @param id
     */
    void updateSubHeadcountById(@Param("id") Long id);


    /**
     * 条件查询完税凭证子任务总数
     * @param taskSubProofBO
     * @return
     */
    Integer querySubProofTotalNumByTaskType(TaskSubProofBO taskSubProofBO);

    /**
     * 查询完税凭证子任务(分页)
     * @param page
     * @param taskSubProofBO
     * @return
     */
    List<TaskSubProofBO> querySubProofInfoByTaskType(Pagination page, TaskSubProofBO taskSubProofBO);

    /**
     * 根据主键ID数组查询完税凭证子任务集合
     * @param subProofIds
     * @return
     */
    List<TaskSubProofPO> querySubTaskProofBySubIds(@Param("subProofIds") String[] subProofIds);

    /**
     * 根据主键ID数组修改完税凭证子任务的合并后ID
     * @param taskSubProofId
     * @param subProofIds
     * @param modifiedBy
     * @return
     */
    Boolean updateSubTaskProofBySubIds(@Param("taskSubProofId") Long taskSubProofId,@Param("subProofIds") List<Long> subProofIds, @Param("modifiedBy") String modifiedBy);

    /**
     * 根据合并的ID拼接的in条件查询合并前的子任务ID
     * @param sbCombinedParams
     * @return
     */
    List<Long> querySubIdsByCombinedIds(@Param("sbCombinedParams") String sbCombinedParams);

    /**
     * 根据合并的ID拼接的in条件将符合条件的任务ID置为不可用状态
     * @param sbCombinedParams
     * @return
     */
    Boolean updateSubIdsByCombinedIds(@Param("sbCombinedParams") String sbCombinedParams);

    /**
     * 置空原子任务的合并ID
     * @param taskSubProofPO
     * @return
     */
    Boolean updateSubTaskProofSubId(TaskSubProofPO taskSubProofPO);


    /**
     * 根据完税凭证子任务ID数组修改其状态
     * @param subIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateTaskProofStatusByIds(@Param("subIds") String[] subIds,@Param("status") String status ,@Param("modifiedBy") String modifiedBy);

    /**
     * 根据完税凭证合并ID数组修改其状态
     * @param subProofIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateTaskProofStatusBySubIds(@Param("subProofIds") String[] subProofIds,@Param("status") String status ,@Param("modifiedBy") String modifiedBy);

    /**
     * 根据主键ID查询完税凭证子任务详细信息
     * @param subProofId
     * @return
     */
    TaskSubProofBO queryApplyDetailsBySubId(@Param("subProofId") Long subProofId);

}