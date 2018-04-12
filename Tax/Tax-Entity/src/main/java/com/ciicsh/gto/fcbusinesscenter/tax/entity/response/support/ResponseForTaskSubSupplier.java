package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;

import java.util.List;

/**
 * @author yuantongqing on 2018/2/9
 */
public class ResponseForTaskSubSupplier extends PageInfo {
    private List<TaskSubSupplierPO> rowList;

    public List<TaskSubSupplierPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubSupplierPO> rowList) {
        this.rowList = rowList;
    }
}
