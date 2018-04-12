package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;

import java.util.List;

/**
 * @author wuhua
 */
public interface TaskSubDeclareService {

    /**
     * 查询申报子任务
     * @param requestForTaskSubDeclare
     * @return
     */
    ResponseForTaskSubDeclare queryTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare);

    /**
     * 合并申报子任务
     * @param requestForTaskSubDeclare
     */
    void mergeTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare);


    /**
     * 拆分申报子任务
     * @param requestForTaskSubDeclare
     * @param type
     * @return
     */
    List<Long> splitSubDeclare(RequestForTaskSubDeclare requestForTaskSubDeclare, String type);

    /**
     * 根据申报子任务ID查询申报信息
     * @param subDeclareId
     * @return
     */
    TaskSubDeclarePO queryTaskSubDeclaresById(long subDeclareId);

    /**
     * 批量完成申报任务
     * @param requestForTaskSubDeclare
     */
    void completeTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare);

    /**
     * 根据ID查询合并之前的申报子任务
     * @param mergeId
     * @return
     */
    List<TaskSubDeclarePO> queryTaskSubDeclareByMergeId(long mergeId);

}

