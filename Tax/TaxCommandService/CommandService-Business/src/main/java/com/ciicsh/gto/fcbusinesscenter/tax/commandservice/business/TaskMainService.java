package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMainDetail;

/**
 * @author wuhua
 */
public interface TaskMainService {

    /**
     * 查询主任务
     * @param
     * @return
     */
    ResponseForTaskMain queryTaskMains(RequestForTaskMain requestForTaskMain);
    /**
     * 查询主任务（草稿）
     * @param
     * @return
     */
    ResponseForTaskMain queryTaskMainsForDraft(RequestForTaskMain requestForTaskMain);
    /**
     * 查询主任务（审核）
     * @param
     * @return
     */
    ResponseForTaskMain queryTaskMainsForCheck(RequestForTaskMain requestForTaskMain);
    /**
     * 提交主任务
     * @param
     * @return
     */
    ResponseForTaskMain submitTaskMains(RequestForTaskMain requestForTaskMain);
    /**
     * 审批通过主任务
     * @param
     * @return
     */
    ResponseForTaskMain passTaskMains(RequestForTaskMain requestForTaskMain);
    /**
     * 失效主任务
     * @param
     * @return
     */
    ResponseForTaskMain invalidTaskMains(RequestForTaskMain requestForTaskMain);
    /**
     * 退回主任务
     * @param
     * @return
     */
    ResponseForTaskMain rejectTaskMains(RequestForTaskMain requestForTaskMain);
    /**
     * 查询主任务明细
     * @param requestForTaskMain
     * @return
     */
    ResponseForTaskMainDetail queryTaskMainDetails(RequestForTaskMain requestForTaskMain);
    /**
     * 更新主任务状态(子任务退回)
     * @param taskMainId
     * @return
     */
    void updateTaskMainStatus(Long taskMainId);
}

