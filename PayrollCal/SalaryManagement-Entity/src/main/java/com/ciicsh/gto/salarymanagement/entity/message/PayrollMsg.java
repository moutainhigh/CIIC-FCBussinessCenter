package com.ciicsh.gto.salarymanagement.entity.message;

import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;

/**
 * Created by bill on 17/12/5.
 */
public class PayrollMsg {

    private String batchCode;

    //操作类型：增加，更新，删除
    private int operateType;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }
}
