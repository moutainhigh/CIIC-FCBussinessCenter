package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * @author yuantongqing on 2018/1/31
 */
@Service
public interface TaxSink {

    /**
     * 结算中心划款topic
     */
    String PAY_APPLY_PAY_STATUS_STREAM_INPUT_CHANNEL = "pay-apply-pay-status-stream-input-channel";
    /**
     * 计算引擎关账topic
     */
    String PR_COMPUTE_CLOSE_OUTPUT_CHANNEL = "pr-compute-close-output-channel";
    /**
     * 计算引擎取消关账topic
     */
    String PR_COMPUTE_UNCLOSE_OUTPUT_CHANNEL = "pr-compute-unclose-output-channel";
    /**
     * 工作流(完成任务)
     */
    String WORKFLOW_CHANNEL_TASK = "common_taskservice_fc_tax_main_task_audit";
    /**
     * 工作流(完成流程)
     */
    String WORKFLOW_CHANNEL_PROCESS_COMPLETE = "common_taskservice_process_complete";


    /**
     * 监听结算中心划款消息
     * @return
     */
    @Input(PAY_APPLY_PAY_STATUS_STREAM_INPUT_CHANNEL)
    MessageChannel moneyMessage();

    /**
     * 监听计算引擎关账消息
     * @return
     */
    @Input(PR_COMPUTE_CLOSE_OUTPUT_CHANNEL)
    MessageChannel clMessage();

    /**
     * 监听计算引擎取消关账消息
     * @return
     */
    @Input(PR_COMPUTE_UNCLOSE_OUTPUT_CHANNEL)
    MessageChannel unclMessage();
    /**
     * 监听工作引擎消息(任务完成)
     * @return
     */
    @Input(WORKFLOW_CHANNEL_TASK)
    MessageChannel wfTaskCompleteMessage();
    /**
     * 监听工作引擎消息(流程完成)
     * @return
     */
    @Input(WORKFLOW_CHANNEL_PROCESS_COMPLETE)
    MessageChannel wfProcessCompleteMessage();

}
