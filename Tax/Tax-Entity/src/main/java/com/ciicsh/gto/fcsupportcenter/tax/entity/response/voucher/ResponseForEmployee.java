package com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher;

import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.EmployeeBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/15
 */
public class ResponseForEmployee extends PageInfo {
    private List<EmployeeBO> rowList;

    public List<EmployeeBO> getRowList() {
        return rowList;
    }

    public void setRowList(List<EmployeeBO> rowList) {
        this.rowList = rowList;
    }
}
