package com.ciicsh.gto.fcsupportcenter.tax.entity.request.data;


import com.ciicsh.gto.fcsupportcenter.tax.entity.page.PageInfo;

/**
 * @author wuhua
 */
public class RequestForCalBatch extends PageInfo {

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
