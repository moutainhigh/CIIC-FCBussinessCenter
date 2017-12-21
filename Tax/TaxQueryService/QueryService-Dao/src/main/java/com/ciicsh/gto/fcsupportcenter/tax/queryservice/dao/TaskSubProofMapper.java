package com.ciicsh.gto.fcsupportcenter.tax.queryservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
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
    Boolean updateSubTaskProof(@Param("subProofIds") String[] subProofIds,@Param("status") String status,@Param("modifiedBy") String modifiedBy);

    /**
     * 根据主任务ID数组修改（即：提交）完税凭证子任务状态
     * @param mainProofIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateSubTaskProofByMainIds(@Param("mainProofIds") String[] mainProofIds,@Param("status") String status,@Param("modifiedBy") String modifiedBy);


    /**
     * 根据子任务主键ID数组将完税凭证子任务置为失效
     * @param subProofIds
     * @param modifiedBy
     * @return
     */
    Boolean invalidSubTaskProofByIds(@Param("subProofIds") String[] subProofIds,@Param("modifiedBy") String modifiedBy);

    /**
     * 根据主任务ID数组将完税凭证子任务置为失效
     * @param mainProofIds
     * @param modifiedBy
     * @return
     */
    Boolean invalidSubTaskProofByMainIds(@Param("mainProofIds") String[] mainProofIds,@Param("modifiedBy") String modifiedBy);

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

}