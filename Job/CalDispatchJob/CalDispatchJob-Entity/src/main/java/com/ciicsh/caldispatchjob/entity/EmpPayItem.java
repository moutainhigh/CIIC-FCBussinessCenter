package com.ciicsh.caldispatchjob.entity;

import java.util.Map;

/**
 * Created by bill on 18/1/16.
 */
public class EmpPayItem {

    private String empCode; // 雇员编码

    private String companyId; // 公司编码

    private Map<String,Object> items; //薪资项名称，薪资项值

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public Map<String, Object> getItems() {
        return items;
    }

    public void setItems(Map<String, Object> items) {
        this.items = items;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
