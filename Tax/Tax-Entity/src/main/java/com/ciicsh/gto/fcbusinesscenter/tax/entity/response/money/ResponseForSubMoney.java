package com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.page.PageInfo;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;

import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/8
 * 划款返回对象
 */
public class ResponseForSubMoney extends PageInfo {

    private List<TaskSubMoneyPO> rowList;

    public List<TaskSubMoneyPO> getRowList() {
        return rowList;
    }

    public void setRowList(List<TaskSubMoneyPO> rowList) {
        this.rowList = rowList;
    }
}
