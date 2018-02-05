package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;

import java.util.List;

/**
 * @author wuhua
 */
public class ResponseForTaskMain extends PageInfo {
    private List<TaskMainPO> rowList;

    public List<TaskMainPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskMainPO> rowList) {
        this.rowList = rowList;
    }
}
