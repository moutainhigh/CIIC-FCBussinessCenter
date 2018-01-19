package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 划款子任务 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2018-01-08
 */
public interface TaskSubMoneyMapper extends BaseMapper<TaskSubMoneyPO> {

    /**
     * 根据主键ID数组修改划款子任务状态
     * @param getSubMoneyIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateTaskSubMoneyStatus(@Param("getSubMoneyIds") String[] getSubMoneyIds, @Param("status") String status, @Param("modifiedBy") String modifiedBy);
}