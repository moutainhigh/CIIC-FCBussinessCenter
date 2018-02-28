package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;

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

}

