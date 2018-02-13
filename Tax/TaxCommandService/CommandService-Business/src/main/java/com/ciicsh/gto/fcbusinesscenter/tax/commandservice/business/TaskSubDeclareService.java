package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;

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
     */
    void splitSubDeclare(RequestForTaskSubDeclare requestForTaskSubDeclare);

    /**
     * 根据申报子任务ID查询申报信息
     * @param subDeclareId
     * @return
     */
    TaskSubDeclarePO queryTaskSubDeclaresById(long subDeclareId);

}

