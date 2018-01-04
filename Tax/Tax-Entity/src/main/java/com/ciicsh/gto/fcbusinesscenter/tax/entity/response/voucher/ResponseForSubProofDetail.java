package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/29
 */
public class ResponseForSubProofDetail extends PageInfo {

    private List<TaskSubProofDetailPO> rowList;

    public List<TaskSubProofDetailPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubProofDetailPO> rowList) {
        this.rowList = rowList;
    }
}
