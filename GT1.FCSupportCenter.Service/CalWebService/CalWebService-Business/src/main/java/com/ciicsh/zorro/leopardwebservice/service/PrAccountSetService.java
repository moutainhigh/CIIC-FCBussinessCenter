package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.dao.IPrAccountSetMapper;
import com.ciicsh.zorro.leopardwebservice.entity.PrAccountSetEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/11/1.
 */
@Service
public class PrAccountSetService implements IPrAccountSetService{

    @Autowired
    private IPrAccountSetMapper prAccountSetMapper;

    static final private int PAGE_SIZE = 10;

    @Override
    public int addItem(PrAccountSetEntity param) {
        int result = 0;
        try {
            result = prAccountSetMapper.insertItem(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteItemById(PrAccountSetEntity param) {
        int result = 0;
        try {
            result = prAccountSetMapper.deleteItemById(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<PrAccountSetEntity> getList(PrAccountSetEntity param, Integer pageNum) {
        List<PrAccountSetEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prAccountSetMapper.selectList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrAccountSetEntity> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public PrAccountSetEntity getItemById(String id) {
        PrAccountSetEntity result = new PrAccountSetEntity();
        try {
            result = prAccountSetMapper.selectItemById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int updateItemById(PrAccountSetEntity param) {
        int result = 0;
        try {
            result = prAccountSetMapper.updateItemById(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> getNameList(String managementId) {
        List<String> resultList = new ArrayList<>();
        try {
            resultList = prAccountSetMapper.selectNameList(managementId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
