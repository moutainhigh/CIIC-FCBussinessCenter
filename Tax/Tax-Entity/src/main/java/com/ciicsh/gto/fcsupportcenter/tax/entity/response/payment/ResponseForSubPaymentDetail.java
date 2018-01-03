package com.ciicsh.gto.fcsupportcenter.tax.entity.response.payment;

import com.ciicsh.gto.fcsupportcenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubPaymentDetailPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofDetailPO;

import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/3
 */
public class ResponseForSubPaymentDetail extends PageInfo {

    private List<TaskSubPaymentDetailPO> rowList;

    public List<TaskSubPaymentDetailPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubPaymentDetailPO> rowList) {
        this.rowList = rowList;
    }
}
