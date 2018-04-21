package com.ciicsh.gto.salarymanagement.entity.message;

import com.ciicsh.gto.salarymanagement.entity.enums.OperateTypeEnum;

/**
 * Created by bill on 18/4/21.
 */
public class AdjustBatchMsg {
    private String rootBatchCode;
    private String originBatchCode;
    private String adjustBatchCode;
    private OperateTypeEnum operateTypeEnum; //操作类型：新增调整／删除调整

    public String getRootBatchCode() {
        return rootBatchCode;
    }

    public void setRootBatchCode(String rootBatchCode) {
        this.rootBatchCode = rootBatchCode;
    }

    public String getOriginBatchCode() {
        return originBatchCode;
    }

    public void setOriginBatchCode(String originBatchCode) {
        this.originBatchCode = originBatchCode;
    }

    public String getAdjustBatchCode() {
        return adjustBatchCode;
    }

    public void setAdjustBatchCode(String adjustBatchCode) {
        this.adjustBatchCode = adjustBatchCode;
    }

    public OperateTypeEnum getOperateTypeEnum() {
        return operateTypeEnum;
    }

    public void setOperateTypeEnum(OperateTypeEnum operateTypeEnum) {
        this.operateTypeEnum = operateTypeEnum;
    }

}
