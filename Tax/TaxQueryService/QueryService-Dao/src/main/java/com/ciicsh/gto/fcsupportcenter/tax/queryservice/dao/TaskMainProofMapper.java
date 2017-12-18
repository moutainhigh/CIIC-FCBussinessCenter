package com.ciicsh.gto.fcsupportcenter.tax.queryservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskMainProofPO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 完税凭证主任务 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-07
 */
public interface TaskMainProofMapper extends BaseMapper<TaskMainProofPO> {

    /**
     * 新增完税凭证主任务
     *
     * @param taskMainProofPO
     * @return
     */
    int addTaskMainProof(TaskMainProofPO taskMainProofPO);


    /**
     * 修改（即：提交/失效）完税凭证主任务状态
     * @param mainProofIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateMainTaskProof(@Param("mainProofIds") String[] mainProofIds,@Param("status") String status,@Param("modifiedBy") String modifiedBy);


    /**
     * 将完税凭证主任务置为失效
     * @param mainProofIds
     * @param modifiedBy
     * @return
     */
    Boolean invalidMainTaskProofByIds(@Param("mainProofIds") String[] mainProofIds,@Param("modifiedBy") String modifiedBy);

}