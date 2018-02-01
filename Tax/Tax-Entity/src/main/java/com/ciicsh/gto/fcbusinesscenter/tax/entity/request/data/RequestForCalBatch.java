package com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

/**
 * @author wuhua
 */
public class RequestForCalBatch extends PageInfo {

    private String t;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    /**
     * 管理方名称
     */
    private String managerName;
    /**
     * 计算批次号
     */
    private String batchNo;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
