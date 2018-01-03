package com.ciicsh.gto.fcbusinesscenter.webcommandservice.business;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.po.WorkingCalendarPO;
import com.baomidou.mybatisplus.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工作日历表 服务类
 * </p>
 *
 * @author guwei
 * @since 27-12-27
 */
public interface WorkingCalendarService extends IService<WorkingCalendarPO> {

    /**
     * 查询工作日历列表
     * @param workingCalendarPO
     * @param page
     * @return
     */
    List<WorkingCalendarPO> getWorkingCalendarList(WorkingCalendarPO workingCalendarPO, Page page);

    /**
     * 查询工作日历详情
     * @param cmyFcWorkingCalendarId
     * @return
     */
    @Override
    WorkingCalendarPO selectById(Serializable cmyFcWorkingCalendarId);

    /**
     * 新增工作日历
     * @param workingCalendarPO
     * @return
     */
    int addWorkingCalendar(WorkingCalendarPO workingCalendarPO);

    /**
     * 更新工作日历
     * @param workingCalendarPO
     * @return
     */
    int updateWorkingCalendar(WorkingCalendarPO workingCalendarPO);

    /**
     * 删除工作日历
     * @param id
     * @return
     */
    int deleteWorkingCalendar(int id);
}
