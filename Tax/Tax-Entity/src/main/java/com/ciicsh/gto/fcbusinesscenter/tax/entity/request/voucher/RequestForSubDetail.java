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

    /**
     * 删除的任务ID
     */
    private Integer[] oldDeleteIds;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 管理方编号
     */
    private String managerNo;

    /**
     * 管理方名称
     */
    private String managerName;

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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
