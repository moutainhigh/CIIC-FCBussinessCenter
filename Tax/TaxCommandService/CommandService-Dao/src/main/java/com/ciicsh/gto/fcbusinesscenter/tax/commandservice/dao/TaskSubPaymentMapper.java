package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentPO;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
  * 缴纳子任务 Mapper 接口
 * </p>
 *
 * @author yuantongqing
 * @since 2018-01-02
 */
public interface TaskSubPaymentMapper extends BaseMapper<TaskSubPaymentPO> {

    /**
     * 修改缴纳子任务任务状态
     * @param subPaymentIds
     * @param status
     * @param modifiedBy
     * @return
     */
    Boolean updateTaskSubPaymentStatus(@Param("subPaymentIds") String[] subPaymentIds, @Param("status") String status, @Param("modifiedBy") String modifiedBy);

}