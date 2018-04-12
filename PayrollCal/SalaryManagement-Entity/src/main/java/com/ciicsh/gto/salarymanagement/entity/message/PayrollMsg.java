package com.ciicsh.gto.salarymanagement.entity.message;

import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;

/**
 * Created by bill on 17/12/5.
 */
public class PayrollMsg {

    private String batchCode;

    //操作类型：增加，更新，删除
    private int operateType;

    //批次类型 1 正常，2 调整， 3 回溯
    private int batchType;

    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }

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

    public String toString(){
        return String.format("批次号：%s --批次类型：%d --操作类型：%d",batchCode,batchType,operateType);
    }

}
