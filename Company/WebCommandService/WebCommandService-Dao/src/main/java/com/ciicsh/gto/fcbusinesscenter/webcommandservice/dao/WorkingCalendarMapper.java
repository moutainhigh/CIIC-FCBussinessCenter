package com.ciicsh.gto.fcbusinesscenter.webcommandservice.dao;

import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.po.WorkingCalendarPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
  * 工作日历 Mapper 接口
 * </p>
 *
 * @author guwei
 * @since 2017-12-27
 */
public interface WorkingCalendarMapper extends BaseMapper<WorkingCalendarPO> {

    /**
     * 查询工作日历列表
     * @param workingCalendarPO
     * @return
     */
    List<WorkingCalendarPO> getList(WorkingCalendarPO workingCalendarPO);
}