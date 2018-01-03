package com.ciicsh.gto.fcsupportcenter.tax.entity.response.payment;

import com.ciicsh.gto.fcsupportcenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubPaymentPO;

import java.util.List;

/**
 * @author yuantongqing on 2018/01/02
 */
public class ResponseForSubPayment extends PageInfo {

    private List<TaskSubPaymentPO> rowList;

    public List<TaskSubPaymentPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubPaymentPO> rowList) {
        this.rowList = rowList;
    }
}
