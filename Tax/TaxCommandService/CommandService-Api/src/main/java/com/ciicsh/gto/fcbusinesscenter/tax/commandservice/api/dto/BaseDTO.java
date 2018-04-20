package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

import java.util.List;

/**
 * @author yuantongqing 2018/4/19
 */
public class BaseDTO {
    /**
     * 管理方信息
     */
    private List<ManagerDTO> managerDTOList;

    public List<ManagerDTO> getManagerDTOList() {
        return managerDTOList;
    }

    public void setManagerDTOList(List<ManagerDTO> managerDTOList) {
        this.managerDTOList = managerDTOList;
    }

    @Override
    public String toString() {
        return "BaseDTO{" +
                "managerDTOList=" + managerDTOList +
                '}';
    }
}
