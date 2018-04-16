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

    static final String TOPIC_PREFIX = "salarygrant_taskservice_";

    /**
     * 创建薪资发放工作流TOPIC
     */
    String SALARY_GRANT_MAIN_TASK_CREATE_WORK_FLOW = TOPIC_PREFIX + "salary_grant_main_task_create_work_flow";

    /**
     * 创建薪资发放任务单TOPIC
     */
    String SALARY_GRANT_MAIN_TASK_CREATE_TASK = TOPIC_PREFIX + "salary_grant_main_task_create_task";

    @Input(SALARY_GRANT_MAIN_TASK_CREATE_WORK_FLOW)
    MessageChannel salaryGrantMainTaskCreateWorkFlow();

    @Input(SALARY_GRANT_MAIN_TASK_CREATE_TASK)
    MessageChannel salaryGrantMainTaskCreateTask();
}
