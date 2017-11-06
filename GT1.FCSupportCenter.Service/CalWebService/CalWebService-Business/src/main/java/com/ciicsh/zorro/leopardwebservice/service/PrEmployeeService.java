package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.dao.IPrEmployeeMapper;
import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/31.
 */
@Service
public class PrEmployeeService implements IPrEmployeeService{

    @Autowired
    private IPrEmployeeMapper prEmployeeMapper;

    final static int PAGE_SIZE = 10;

    @Override
    public PageInfo<PrEmployeeEntity> getEmployeeList(PrEmployeeEntity param, Integer pageNum) {

        List<PrEmployeeEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prEmployeeMapper.selectList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrEmployeeEntity> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }
}
