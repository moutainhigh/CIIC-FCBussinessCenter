package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author wuhua
 */
public class ResponseForCalBatchDetail extends PageInfo {
    private List<CalculationBatchDetailBO> rowList;

    public List<CalculationBatchDetailBO> getRowList() {
        return rowList;
    }

    public void setRowList(List<CalculationBatchDetailBO> rowList) {
        this.rowList = rowList;
    }
}
