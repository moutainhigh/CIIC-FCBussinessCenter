package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsMainTaskPO;
import org.bson.Document;

/**
 * 工资单任务单主表 服务类
 *
 * @author taka
 * @since 2018-02-11
 */
public interface PrsMainTaskService {
    List<PrsMainTaskPO> listPrsMainTasks(Map<String, Object> params);

    Page<PrsMainTaskPO> pagePrsMainTasks(Map<String, Object> params);

    PrsMainTaskPO getPrsMainTask(Map<String, Object> params);

    List<Document> getTaskEmps(Map<String, Object> params);

    Boolean addPrsMainTask(Map<String, Object> params);

    Boolean updatePrsMainTask(Map<String, Object> params);
}
