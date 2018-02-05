package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 完税申请明细 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2017-12-07
 */
public interface TaskSubProofDetailMapper extends BaseMapper<TaskSubProofDetailPO> {

    /**
     * 通过主任务查询完税申请明细
     * @param taskSubProofDetailBO
     * @return
     */
    List<TaskSubProofDetailPO> queryTaskSubProofDetailByMainId(TaskSubProofDetailBO taskSubProofDetailBO);

    /**
     * 通过子任务查询完税申请明细
     * @param taskSubProofDetailBO
     * @return
     */
    List<TaskSubProofDetailPO> queryTaskSubProofDetailBySubId(TaskSubProofDetailBO taskSubProofDetailBO);

}