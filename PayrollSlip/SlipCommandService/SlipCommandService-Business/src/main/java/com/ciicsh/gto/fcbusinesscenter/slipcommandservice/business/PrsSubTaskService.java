package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsSubTaskPO;

/**
 * 工资单子任务单主表 服务类
 *
 * @author taka
 * @since 2018-02-11
 */
public interface PrsSubTaskService {
    List<PrsSubTaskPO> listPrsSubTasks(Map<String, Object> params);

    Page<PrsSubTaskPO> pagePrsSubTasks(Map<String, Object> params);

    PrsSubTaskPO getPrsSubTask(Map<String, Object> params);

    Boolean addPrsSubTask(Map<String, Object> params);

    Boolean updatePrsSubTask(Map<String, Object> params);

    Boolean updatePrsSubTaskByMainTaskId(Map<String, Object> params);
}
