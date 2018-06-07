package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;

/**
 * <p>
 * 薪资发放任务单子表工作流调用 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-06-07
 */
public interface SalaryGrantSubTaskWorkFlowService extends IService<SalaryGrantSubTaskPO> {

    /**
     * 任务单流程--提交（调用工作流引擎）
     * @return
     */
    Boolean doSubmitTask();

    /**
     * 任务单流程--审批通过（调用工作流引擎）
     * @return
     */
    Boolean doApproveTask();

    /**
     * 任务单流程--退回（调用工作流引擎）
     * @return
     */
    Boolean doReturnTask();

    /**
     * 任务单流程--撤回（调用工作流引擎）
     * @return
     */
    Boolean doRetreatTask();
}
