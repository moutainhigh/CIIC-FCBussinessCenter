package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;

import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/2/8
 */
public class ResponseForSubDeclareDetail extends PageInfo {
    private List<TaskSubDeclareDetailPO> rowList;

    public List<TaskSubDeclareDetailPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubDeclareDetailPO> rowList) {
        this.rowList = rowList;
    }
}
