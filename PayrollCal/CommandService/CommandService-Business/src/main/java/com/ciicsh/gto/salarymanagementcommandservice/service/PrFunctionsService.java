package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrFunctionsPO;
import com.github.pagehelper.PageInfo;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-8-10 17:43:49
 * @description 标准函数库Service
 */
public interface PrFunctionsService {

    /**
     * 查询函数列表
     * @param prFunctionsPO 查询条件
     * @param pageNum 分页数
     * @param pageSize 每页显示数量
     * @return 查询列表
     */
    PageInfo<PrFunctionsPO> getFunctionsListByName(PrFunctionsPO prFunctionsPO, Integer pageNum, Integer pageSize);

}
