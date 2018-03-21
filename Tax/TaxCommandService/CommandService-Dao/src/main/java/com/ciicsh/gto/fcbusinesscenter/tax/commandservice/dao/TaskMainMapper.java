package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wuhua
 */
public interface TaskMainMapper extends BaseMapper<TaskMainPO> {

    /**
     * 查询主任务明细列表
     * @param taskMainId
     * @return
     */
    List<TaskMainDetailBO> queryTaskMainDetails(@Param("taskMainId")Long taskMainId);
}