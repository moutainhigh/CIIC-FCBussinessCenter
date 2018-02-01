package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyDetailPO;

import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/3
 * 划款详情返回对象
 */
public class ResponseForSubMoneyDetail extends PageInfo {

    private List<TaskSubMoneyDetailPO> rowList;

    public List<TaskSubMoneyDetailPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubMoneyDetailPO> rowList) {
        this.rowList = rowList;
    }
}
