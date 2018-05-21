package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowTaskInfoBO;

import java.util.List;

/**
 * <p>
 * 薪资发放任务单查询 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
public interface SalaryGrantTaskQueryService {

    /**
     * 查询薪资发放任务单列表
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> salaryGrantList(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 根据任务单编号查询任务单
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return
     */
    SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 根据任务单编号查询操作记录
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return
     */
    Page<WorkFlowTaskInfoBO> operation(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * @description 根据主表任务单编号查询子表任务单
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return
     */
    List<SalaryGrantTaskBO> querySubTask(SalaryGrantTaskBO salaryGrantTaskBO);
}
