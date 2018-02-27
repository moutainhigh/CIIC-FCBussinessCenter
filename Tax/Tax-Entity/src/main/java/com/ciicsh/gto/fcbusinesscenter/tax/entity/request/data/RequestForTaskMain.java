package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author wuhua
 */
public class RequestForTaskMain extends PageInfo {

    private String[] batchIds;

    private String[] batchNos;

    private String[] taskMainIds;

    public String[] getTaskMainIds() {
        return taskMainIds;
    }

    public void setTaskMainIds(String[] taskMainIds) {
        this.taskMainIds = taskMainIds;
    }

    public String[] getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(String[] batchNos) {
        this.batchNos = batchNos;
    }

    public String[] getBatchIds() {
        return batchIds;
    }

    public void setBatchIds(String[] batchIds) {
        this.batchIds = batchIds;
    }
}
