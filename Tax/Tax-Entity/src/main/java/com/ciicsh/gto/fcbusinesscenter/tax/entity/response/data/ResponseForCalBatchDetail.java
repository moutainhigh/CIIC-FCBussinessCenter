package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author wuhua
 */
public class ResponseForCalBatchDetail extends PageInfo {
    private List<CalculationBatchDetailPO> rowList;

    public List<CalculationBatchDetailPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<CalculationBatchDetailPO> rowList) {
        this.rowList = rowList;
    }
}
