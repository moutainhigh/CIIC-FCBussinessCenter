package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.file;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.FilePO;

import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/5
 */
public class ResponseForFile extends PageInfo {
    private List<FilePO> rowList;

    public List<FilePO> getRowList() {
        return rowList;
    }

    public void setRowList(List<FilePO> rowList) {
        this.rowList = rowList;
    }
}
