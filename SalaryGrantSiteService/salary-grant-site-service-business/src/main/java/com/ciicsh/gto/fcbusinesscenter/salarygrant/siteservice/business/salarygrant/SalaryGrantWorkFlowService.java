package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;



import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskMissionRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;

import java.util.Map;

/**
 * <p>
 * 薪资发放工作流调用 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
public interface SalaryGrantWorkFlowService {

    /**
     *  调用工作流引擎，启动薪资发放任务单的工作流。
     * @param salaryGrantTaskMissionRequestDTO
     * @return Map
     */
    Map startSalaryGrantTaskProcess(SalaryGrantTaskMissionRequestDTO salaryGrantTaskMissionRequestDTO);

    /**
     *  调用工作流引擎，获取工作流的流程编号。
     * @param startProcessResponseMap
     * @return String
     */
    String getProcessId(Map startProcessResponseMap);

    /**
     *  调用工作流引擎，获取工作流的流程编号。
     * @param salaryGrantTaskRequestDTO
     * @return Map
     */
    Map completeSalaryGrantTask(SalaryGrantTaskRequestDTO salaryGrantTaskRequestDTO);

    // todo
    // 任务单流程--提交（调用工作流引擎）
    //Boolean doSubmitTask();

    // todo
    // 任务单流程--审批通过（调用工作流引擎）
    //Boolean doApproveTask();

    // todo
    // 任务单流程--退回（调用工作流引擎）
    //Boolean doReturnTask();

    // todo
    // 任务单流程--撤回（调用工作流引擎）
    //Boolean doRetreatTask();

    // todo
    // 任务单流程--驳回（调用工作流引擎）
    //Boolean doRejectTask();

    // todo
    // 任务单流程--插入流程日志（调用工作流引擎，把消息返回的信息插入到业务系统的工作流任务日志表中）
    //boolean doCreateTaskLog();

    // todo
    // 任务单流程--查看流程日志（对业务系统的工作流任务日志表进行查询，工作流引擎目前不提供维护流程日志的功能）
    //List querySalaryGrantWorkFlowLog(Map paramMap);

    /**
     * 流程节点处理：1.失效操作
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doCancelTask(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：2.提交 - 修改薪资发放日和薪资发放时段信息
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean updateGrantDateAndTime(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：2.提交 - 判断是否逾期
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean isOverdue(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：2.提交处理总方法
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doSubmitTask(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：3.退回
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doReturnTask (SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：4.通过
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doApproveTask (SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：5.撤回
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doRetreatTask (SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：6.驳回
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doRejectTask (SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 流程节点处理：7、作废
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doInvalidTask (SalaryGrantTaskBO salaryGrantTaskBO);
}
