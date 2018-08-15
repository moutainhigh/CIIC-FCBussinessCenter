package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagement.entity.po.PrFunctionsPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrFunctionsMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrFunctionsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-08-10 17:47
 * @description 标准函数库ServiceImpl
 */
@Service
public class PrFunctionsServiceImpl implements PrFunctionsService {

    @Autowired
    private PrFunctionsMapper prFunctionsMaper;

    @Override
    public PageInfo<PrFunctionsPO> getFunctionsListByName(PrFunctionsPO prFunctionsPO, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<PrFunctionsPO> resultList = prFunctionsMaper.getFunctionsListByName(prFunctionsPO.getName());
        return new PageInfo<>(resultList);
    }
}
