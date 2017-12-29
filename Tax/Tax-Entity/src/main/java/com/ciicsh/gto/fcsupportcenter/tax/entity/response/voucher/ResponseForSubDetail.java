package com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher;

import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.page.PageInfo;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/14
 */
public class ResponseForSubDetail extends PageInfo {

    private List<TaskSubProofDetailBO> rowList;

    public List<TaskSubProofDetailBO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubProofDetailBO> rowList) {
        this.rowList = rowList;
    }
}
