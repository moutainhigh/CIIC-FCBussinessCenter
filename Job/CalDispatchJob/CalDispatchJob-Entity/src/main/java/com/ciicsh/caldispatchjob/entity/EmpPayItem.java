package com.ciicsh.caldispatchjob.entity;

import java.util.HashMap;

/**
 * Created by bill on 18/1/16.
 */
public class EmpPayItem {

    private String empCode; // 雇员编码

    private HashMap<String,Object> items; //薪资项名称，薪资项值

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public HashMap<String, Object> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Object> items) {
        this.items = items;
    }

}
