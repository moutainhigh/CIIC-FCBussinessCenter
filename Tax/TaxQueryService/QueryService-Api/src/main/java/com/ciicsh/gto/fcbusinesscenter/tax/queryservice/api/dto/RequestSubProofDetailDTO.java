package com.ciicsh.gto.fcbusinesscenter.tax.queryservice.api.dto;

import java.util.Arrays;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/16
 */
public class RequestSubProofDetailDTO {

    /**
     * 详情页面的类型，用于判断是主任务模块进入还是子任务模块进入
     */
    private String detailType;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 前端置为失效的完税申请明细id
     */
    private Integer[] oldDeleteIds;


    /**
     * 前端新增的完税申请明细信息
     */
    private List<TaskSubProofDetailDTO> taskSubProofDetailDTOList;

    public Integer[] getOldDeleteIds() {
        return oldDeleteIds;
    }

    public void setOldDeleteIds(Integer[] oldDeleteIds) {
        this.oldDeleteIds = oldDeleteIds;
    }

    public List<TaskSubProofDetailDTO> getTaskSubProofDetailDTOList() {
        return taskSubProofDetailDTOList;
    }

    public void setTaskSubProofDetailDTOList(List<TaskSubProofDetailDTO> taskSubProofDetailDTOList) {
        this.taskSubProofDetailDTOList = taskSubProofDetailDTOList;
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

    @Override
    public String toString() {
        return "RequestSubProofDetailDTO{" +
                "detailType='" + detailType + '\'' +
                ", taskId=" + taskId +
                ", oldDeleteIds=" + Arrays.toString(oldDeleteIds) +
                ", taskSubProofDetailDTOList=" + taskSubProofDetailDTOList +
                '}';
    }
}
