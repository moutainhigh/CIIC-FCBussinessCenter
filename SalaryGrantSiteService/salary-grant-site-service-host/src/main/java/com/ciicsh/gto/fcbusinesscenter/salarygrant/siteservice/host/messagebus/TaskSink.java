package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.messagebus;

import com.ciicsh.gto.sheetservice.api.MsgConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TOPIC信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-25
 */
@Service
public interface TaskSink {
    @Input(MsgConstants.COMMON_TASKSERVICE_TASK_COMPLETE)
    MessageChannel commonTaskserviceTaskComplete();

    @Input(MsgConstants.COMMON_TASKSERVICE_PROCESS_COMPLETE)
    MessageChannel commonTaskserviceProcessComplete();

    static final String TOPIC_PREFIX = "common_taskservice_";

    /**
     * 创建薪资发放任务单SGT工作流TOPIC
     */
    String SALARY_GRANT_MAIN_TASK_CREATE_WORK_FLOW = TOPIC_PREFIX + "payroll_main";
    @Input(SALARY_GRANT_MAIN_TASK_CREATE_WORK_FLOW)
    MessageChannel salaryGrantMainTaskCreateWorkFlow();

    /**
     * 创建薪资发放本地本币任务单LTB工作流TOPIC
     */
    String SALARY_GRANT_SUB_TASK_LTB_CREATE_WORK_FLOW = TOPIC_PREFIX + "payroll_local_domestic_currency";
    @Input(SALARY_GRANT_SUB_TASK_LTB_CREATE_WORK_FLOW)
    MessageChannel salaryGrantSubTaskLTBCreateWorkFlow();

    /**
     * 创建薪资发放本地外币任务单LTW工作流TOPIC
     */
    String SALARY_GRANT_SUB_TASK_LTW_CREATE_WORK_FLOW = TOPIC_PREFIX + "payroll_local_foreign_currency";
    @Input(SALARY_GRANT_SUB_TASK_LTW_CREATE_WORK_FLOW)
    MessageChannel salaryGrantSubTaskLTWCreateWorkFlow();

    /**
     * 创建薪资发放外地任务单ST工作流TOPIC
     */
    String SALARY_GRANT_SUB_TASK_ST_CREATE_WORK_FLOW = TOPIC_PREFIX + "payroll_nonlocal";
    @Input(SALARY_GRANT_SUB_TASK_ST_CREATE_WORK_FLOW)
    MessageChannel salaryGrantSubTaskSTCreateWorkFlow();

    /**
     * 创建供应商支付任务单SPT工作流TOPIC
     */
    String SALARY_GRANT_SUPPLIER_PAYMENT_TASK_CREATE_WORK_FLOW = TOPIC_PREFIX + "supplier_payment";
    @Input(SALARY_GRANT_SUPPLIER_PAYMENT_TASK_CREATE_WORK_FLOW)
    MessageChannel salaryGrantSupplierPaymentTaskCreateWorkFlow();

    /**
     * 创建薪资发放任务单TOPIC
     */
    String SALARY_GRANT_MAIN_TASK_CREATE_TASK = "sg_compute-close-output-channel";
    @Input(SALARY_GRANT_MAIN_TASK_CREATE_TASK)
    MessageChannel salaryGrantMainTaskCreateTask();

    /**
     * 结算中心退票TOPIC
     */
    String SALARY_GRANT_REFUND = "pay-apply-return-ticket-stream";
    @Input(SALARY_GRANT_REFUND)
    MessageChannel salaryGrantRefundProcess();

    /**
     * 结算中心支付TOPIC
     */
    String SALARY_GRANT_PAYMENT = "pay-apply-pay-status-stream";
    @Input(SALARY_GRANT_PAYMENT)
    MessageChannel salaryGrantPaymentProcess();

    /**
     * 失效薪资发放任务单TOPIC
     */
    String SALARY_GRANT_MAIN_TASK_CANCEL_TASK = "sg_compute-unclose-output-channel";
    @Input(SALARY_GRANT_MAIN_TASK_CANCEL_TASK)
    MessageChannel salaryGrantMainTaskCancelTask();
}
