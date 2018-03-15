package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author wuhua
 */
public class ResponseForTaskMainDetail<T> extends PageInfo {
    private List<T> rowList;

    public List<T> getRowList() {
        return rowList;
    }

    public void setRowList(List<T> rowList) {
        this.rowList = rowList;
    }
}
