package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/19
 */
public class ResponseForBatchDetail extends PageInfo {

    private List<CalculationBatchDetailPO> rowList;

    public List<CalculationBatchDetailPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<CalculationBatchDetailPO> rowList) {
        this.rowList = rowList;
    }
}
