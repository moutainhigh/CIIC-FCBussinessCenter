package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;

import java.util.List;

/**
 * @author wuhua
 */
public class ResponseForCalBatch extends PageInfo {
    private List<CalculationBatchPO> rowList;

    public List<CalculationBatchPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<CalculationBatchPO> rowList) {
        this.rowList = rowList;
    }
}
