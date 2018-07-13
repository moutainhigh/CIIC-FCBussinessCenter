package com.ciicsh.gto.salarymanagement.entity.message;


import java.util.List;

/**
 * Created by bill on 17/12/11.
 */
public class PayrollEmpGroup {

    private String empGroupIds; //逗号隔开
    //雇员ID列表
    private String ids;  //逗号隔开

    //操作类型：增加，更新，删除
    private int operateType;

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEmpGroupIds() {
        return empGroupIds;
    }

    public void setEmpGroupIds(String empGroupIds) {
        this.empGroupIds = empGroupIds;
    }

    @Override
    public String toString(){
        return " empGroupIds :" + empGroupIds
                + " ids :" + ids
                + " operateType :" +  String.valueOf(operateType)
                ;
    }
}
