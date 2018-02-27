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
}

