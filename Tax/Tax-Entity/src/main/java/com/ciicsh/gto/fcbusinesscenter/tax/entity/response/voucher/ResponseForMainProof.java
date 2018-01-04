package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/13
 */
public class ResponseForMainProof extends PageInfo {
    private List<TaskMainProofBO> rowList;

    public List<TaskMainProofBO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskMainProofBO> rowList) {
        this.rowList = rowList;
    }
}
