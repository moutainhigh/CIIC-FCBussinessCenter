package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author wuhua
 */
public class RequestForTaskSubDeclare extends PageInfo {

    private Long[] taskSubDeclareId;

    public Long[] getTaskSubDeclareId() {
        return taskSubDeclareId;
    }

    public void setTaskSubDeclareId(Long[] taskSubDeclareId) {
        this.taskSubDeclareId = taskSubDeclareId;
    }
}
