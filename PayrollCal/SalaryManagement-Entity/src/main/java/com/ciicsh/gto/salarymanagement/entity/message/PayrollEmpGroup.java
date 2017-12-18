package com.ciicsh.gto.salarymanagement.entity.message;


import java.util.List;

/**
 * Created by bill on 17/12/11.
 */
public class PayrollEmpGroup {

    private List<String> empGroupIds;
    //雇员ID列表
    private List<String> ids;

    //操作类型：增加，更新，删除
    private int operateType;

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getEmpGroupIds() {
        return empGroupIds;
    }

    public void setEmpGroupIds(List<String> empGroupIds) {
        this.empGroupIds = empGroupIds;
    }
}
