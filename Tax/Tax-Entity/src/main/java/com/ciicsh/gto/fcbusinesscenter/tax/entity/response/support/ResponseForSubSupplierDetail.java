package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierDetailPO;

import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/2/11
 */
public class ResponseForSubSupplierDetail extends PageInfo {
    private List<TaskSubSupplierDetailPO> rowList;

    public List<TaskSubSupplierDetailPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubSupplierDetailPO> rowList) {
        this.rowList = rowList;
    }
}
