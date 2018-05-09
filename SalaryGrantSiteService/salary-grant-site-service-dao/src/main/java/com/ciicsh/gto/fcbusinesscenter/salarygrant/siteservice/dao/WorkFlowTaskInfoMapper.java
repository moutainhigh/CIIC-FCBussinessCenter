package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowTaskInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.WorkFlowTaskInfoPO;

import java.util.List;

/**
 * <p>
  * 工作流任务日志表 Mapper 接口
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-09
 */
public interface WorkFlowTaskInfoMapper extends BaseMapper<WorkFlowTaskInfoPO> {
    /**
     * 根据任务单编号查询操作记录
     * @author chenpb
     * @since 2018-05-07
     * @param page
     * @param bo
     * @return
     */
    List<WorkFlowTaskInfoBO> operation(Pagination page, SalaryGrantTaskBO bo);
}