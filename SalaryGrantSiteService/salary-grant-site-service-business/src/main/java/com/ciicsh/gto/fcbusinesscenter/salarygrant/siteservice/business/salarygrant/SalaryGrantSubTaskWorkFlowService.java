package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

/**
 * <p>
 * 薪资发放任务单子表工作流调用 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-06-07
 */
public interface SalaryGrantSubTaskWorkFlowService {

    // todo
    // 任务单流程--提交（调用工作流引擎）
    Boolean doSubmitTask();

    // todo
    // 任务单流程--审批通过（调用工作流引擎）
    Boolean doApproveTask();

    // todo
    // 任务单流程--退回（调用工作流引擎）
    Boolean doReturnTask();

    // todo
    // 任务单流程--撤回（调用工作流引擎）
    Boolean doRetreatTask();
}
