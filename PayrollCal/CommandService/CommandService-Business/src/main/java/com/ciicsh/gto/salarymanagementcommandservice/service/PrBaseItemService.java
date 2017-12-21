package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollBaseItemPO;

import java.util.List;

/**
 * Created by jiangtianning on 2017/12/20.
 */
public interface PrBaseItemService {

    /**
     * 获取基础项目
     * @param param
     * @return
     */
    PrPayrollBaseItemPO getItem(PrPayrollBaseItemPO param);

    /**
     * 获取基础项目列表
     * @param param
     * @return
     */
    List<PrPayrollBaseItemPO> getList(PrPayrollBaseItemPO param);
}
