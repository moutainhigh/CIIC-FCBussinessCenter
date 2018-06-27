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

    /**
     * 创建薪资发放任务单SGT工作流TOPIC
     */
    @Input(MsgConstants.FC.PAYROLL_MAIN)
    MessageChannel salaryGrantMainTaskCreateWorkFlow();

    /**
     * 创建薪资发放本地本币任务单LTB工作流TOPIC
     */
    @Input(MsgConstants.FC.PAYROLL_LOCAL_DOMESTIC_CURRENCY)
    MessageChannel salaryGrantSubTaskLTBCreateWorkFlow();

    /**
     * 创建薪资发放本地外币任务单LTW工作流TOPIC
     */
    @Input(MsgConstants.FC.PAYROLL_LOCAL_FOREIGN_CURRENCY)
    MessageChannel salaryGrantSubTaskLTWCreateWorkFlow();

    /**
     * 创建薪资发放外地任务单ST工作流TOPIC
     */
    @Input(MsgConstants.FC.PAYROLL_NONLOCAL)
    MessageChannel salaryGrantSubTaskSTACreateWorkFlow();

    /**
     * 创建供应商支付任务单SPT工作流TOPIC
     */
    @Input(MsgConstants.FC.SUPPLIER_PAYMENT)
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
    String SALARY_GRANT_PAYMENT = "pay-apply-pay-status-stream-input-channel";
    @Input(SALARY_GRANT_PAYMENT)
    MessageChannel salaryGrantPaymentProcess();

    /**
     * 取消关帐TOPIC
     */
    String SALARY_GRANT_MAIN_TASK_CANCEL_TASK = "sg_compute-unclose-output-channel";
    @Input(SALARY_GRANT_MAIN_TASK_CANCEL_TASK)
    MessageChannel salaryGrantMainTaskCancelTask();
}
