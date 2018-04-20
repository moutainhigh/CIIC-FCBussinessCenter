package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

/**
 * @author yuantongqing 2018/4/19
 */
public class ManagerDTO {

    /**
     * 管理方编号
     */
    private String managerNo;

    /**
     * 管理方名称
     */
    private String managerName;

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    @Override
    public String toString() {
        return "ManagerDTO{" +
                "managerNo='" + managerNo + '\'' +
                ", managerName='" + managerName + '\'' +
                '}';
    }
}
