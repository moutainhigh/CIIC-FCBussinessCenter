package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;

/**
 * @author wuhua
 */
public interface TaskSubDeclareService {

    /**
     * 查询申报子任务
     * @param
     * @return
     */
    ResponseForTaskSubDeclare queryTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare);
    /**
     * 合并申报子任务
     * @param
     * @return
     */
    ResponseForTaskSubDeclare mergeTaskSubDeclares(RequestForTaskSubDeclare requestForTaskSubDeclare);

}

