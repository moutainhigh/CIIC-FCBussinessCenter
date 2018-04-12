package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;

import java.util.List;

/**
 * @author wuhua
 */
public class ResponseForTaskSubDeclare extends PageInfo {
    private List<TaskSubDeclarePO> rowList;

    public List<TaskSubDeclarePO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubDeclarePO> rowList) {
        this.rowList = rowList;
    }
}
