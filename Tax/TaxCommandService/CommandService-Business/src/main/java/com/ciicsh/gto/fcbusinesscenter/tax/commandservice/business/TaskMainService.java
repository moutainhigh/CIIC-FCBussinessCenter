package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMainDetail;

import java.util.List;

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
     * @param requestForTaskMain
     * @return
     */
    Boolean  rejectTaskMains(RequestForTaskMain requestForTaskMain);
    /**
     * 查询主任务明细
     * @param requestForTaskMain
     * @return
     */
    ResponseForTaskMainDetail queryTaskMainDetails(RequestForTaskMain requestForTaskMain);
    /**
     * 更新主任务状态(子任务退回)
     * @param taskMainIds
     * @return
     */
    Boolean updateTaskMainStatus(Long[] taskMainIds);
    /**
     * 子任务状态是否和主任务状态一致
     * @param taskMainIds
     * @return
     */
    boolean isStatusSame(String[] taskMainIds,String[] status);

    /**
     * 根据主任务ID查询主任务信息
     * @param id
     * @return
     */
    TaskMainPO queryTaskMainById(Long id);

    //更新主任务状态
    void updateTaskMainsStatus(String[] taskMainIds,String status,String[] currentStatus);

    /**
     * 根据主任务ID数组以及状态查询个子任务此状态的数目
     * @param taskMainIds
     * @param status
     * @return
     */
    int querySubTaskNumByMainIdsAndStatus(List<Long> taskMainIds, String status);
}

