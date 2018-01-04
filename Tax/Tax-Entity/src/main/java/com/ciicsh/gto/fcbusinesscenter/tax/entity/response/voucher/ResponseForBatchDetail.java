package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/19
 */
public class ResponseForBatchDetail extends PageInfo {

    private List<CalculationBatchDetailBO> rowList;

    public List<CalculationBatchDetailBO> getRowList() {
        return rowList;
    }

    public void setRowList(List<CalculationBatchDetailBO> rowList) {
        this.rowList = rowList;
    }
}
