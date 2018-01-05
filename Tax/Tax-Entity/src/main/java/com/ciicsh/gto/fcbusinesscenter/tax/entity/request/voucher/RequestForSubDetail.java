package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/16
 */
public class RequestForSubDetail {

    /**
     * 详情页面的类型，用于判断是主任务模块进入还是子任务模块进入
     */
    private String detailType;

    /**
     * 任务ID
     */
    private Long taskId;

    private Integer[] oldDeleteIds;

    private List<TaskSubProofDetailBO> taskSubProofDetailBOList;

    public Integer[] getOldDeleteIds() {
        return oldDeleteIds;
    }

    public void setOldDeleteIds(Integer[] oldDeleteIds) {
        this.oldDeleteIds = oldDeleteIds;
    }

    public List<TaskSubProofDetailBO> getTaskSubProofDetailBOList() {
        return taskSubProofDetailBOList;
    }

    public void setTaskSubProofDetailBOList(List<TaskSubProofDetailBO> taskSubProofDetailBOList) {
        this.taskSubProofDetailBOList = taskSubProofDetailBOList;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
