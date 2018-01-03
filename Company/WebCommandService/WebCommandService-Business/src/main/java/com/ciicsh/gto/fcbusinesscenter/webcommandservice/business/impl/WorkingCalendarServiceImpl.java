package com.ciicsh.gto.fcbusinesscenter.webcommandservice.business.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.po.WorkingCalendarPO;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.business.WorkingCalendarService;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.dao.WorkingCalendarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作日历表 服务实现类
 * </p>
 *
 * @author guwei
 * @since 2017-12-27
 */
@Service("workingCalendarService")
public class WorkingCalendarServiceImpl extends ServiceImpl<WorkingCalendarMapper, WorkingCalendarPO> implements WorkingCalendarService {

    @Autowired
    private WorkingCalendarMapper workingCalendarMapper;

    @Override
    public List<WorkingCalendarPO> getWorkingCalendarList(WorkingCalendarPO workingCalendarPO, Page page) {
        return workingCalendarMapper.getList(workingCalendarPO);
    }

    @Override
    public int addWorkingCalendar(WorkingCalendarPO workingCalendarPO) {
        return workingCalendarMapper.insert(workingCalendarPO);
    }

    @Override
    public int updateWorkingCalendar(WorkingCalendarPO workingCalendarPO) {
        return workingCalendarMapper.updateById(workingCalendarPO);
    }

    @Override
    public int deleteWorkingCalendar(int id) {
        return workingCalendarMapper.deleteById(id);
    }
}
