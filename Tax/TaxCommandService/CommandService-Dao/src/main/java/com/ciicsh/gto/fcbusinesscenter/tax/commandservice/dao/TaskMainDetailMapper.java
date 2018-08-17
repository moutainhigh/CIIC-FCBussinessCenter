package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainDetailPO;

import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
public interface TaskMainDetailMapper extends BaseMapper<TaskMainDetailPO> {

    /**
     * 分页查询主任务明细列表
     * @param params
     * @return
     */
    List<TaskMainDetailBO> queryTaskMainDetails(Pagination page, Map<String,Object> params);

    /**
     * 查询主任务明细列表
     * @param params
     * @return
     */
    List<TaskMainDetailBO> queryTaskMainDetails(Map<String,Object> params);
}