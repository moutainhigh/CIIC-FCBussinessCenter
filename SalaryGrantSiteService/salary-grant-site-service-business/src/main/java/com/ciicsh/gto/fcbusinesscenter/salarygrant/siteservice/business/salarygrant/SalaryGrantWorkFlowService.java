package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;


import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;

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
     * 流程节点处理：1.失效操作
     *
     * @param salaryGrantTaskBO
     * @return
     */
    Boolean doCancelTask(SalaryGrantTaskBO salaryGrantTaskBO) throws Exception;

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
    int doSubmitTask(SalaryGrantTaskBO salaryGrantTaskBO);

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
    Boolean doInvalidTask (SalaryGrantTaskBO salaryGrantTaskBO) throws Exception;
}
