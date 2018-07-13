package com.ciicsh.gto.salarymanagement.entity.dto;

import java.util.List;

/**
 * Created by bill on 17/12/27.
 */
public class SimpleEmpPayItemDTO {

    private String empCode;  //雇员编号

    private String empName; //雇员名称

    private String companyId; //公司ID

    List<SimplePayItemDTO> payItemDTOS; //薪资项列表

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public List<SimplePayItemDTO> getPayItemDTOS() {
        return payItemDTOS;
    }

    public void setPayItemDTOS(List<SimplePayItemDTO> payItemDTOS) {
        this.payItemDTOS = payItemDTOS;
    }

}
