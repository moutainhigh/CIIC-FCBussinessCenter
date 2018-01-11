package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/13
 */
public class ResponseForMainProof extends PageInfo {
    private List<TaskMainProofPO> rowList;

    public List<TaskMainProofPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskMainProofPO> rowList) {
        this.rowList = rowList;
    }
}
