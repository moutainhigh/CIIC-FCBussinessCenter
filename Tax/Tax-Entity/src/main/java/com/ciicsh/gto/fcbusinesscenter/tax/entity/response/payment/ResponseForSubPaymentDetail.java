package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment;



import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentDetailPO;

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
