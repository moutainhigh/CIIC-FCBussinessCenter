package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.dao.IPrGroupMapper;
import com.ciicsh.zorro.leopardwebservice.entity.PrGroupEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/24.
 */
@Service
public class PrGroupService implements IPrGroupService{

    @Autowired
    private IPrGroupMapper prGroupMapper;

    final static int PAGE_SIZE = 5;

    @Override
    public PageInfo<PrGroupEntity> getList(PrGroupEntity paramItem, Integer pageNum) {
        List<PrGroupEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prGroupMapper.selectList(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrGroupEntity> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public PrGroupEntity getItem(PrGroupEntity paramItem) {
        PrGroupEntity result = new PrGroupEntity();
        try {
            result = prGroupMapper.selectItem(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int updateItem(PrGroupEntity paramItem) {
        return prGroupMapper.updateItem(paramItem);
    }

    @Override
    public String addItem(PrGroupEntity paramItem) {
        int result = 0;
        try {
            result = prGroupMapper.insertItem(paramItem);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result == 0 ? "" : paramItem.getCode();
    }

    @Override
    public List<String> getNameList(String managementId) {
        List<String> nameList = prGroupMapper.selectNameList(managementId);
        return nameList;
    }

    @Override
    public PageInfo<PrGroupEntity> getQueryList(PrGroupEntity paramItem, Integer pageNum) {
        List<PrGroupEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prGroupMapper.selectQueryList(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrGroupEntity> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }
}
