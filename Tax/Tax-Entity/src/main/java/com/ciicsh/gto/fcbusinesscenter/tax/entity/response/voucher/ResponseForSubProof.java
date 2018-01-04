package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/13
 */
public class ResponseForSubProof extends PageInfo{
    private List<TaskSubProofBO> rowList;

    public List<TaskSubProofBO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubProofBO> rowList) {
        this.rowList = rowList;
    }
}
