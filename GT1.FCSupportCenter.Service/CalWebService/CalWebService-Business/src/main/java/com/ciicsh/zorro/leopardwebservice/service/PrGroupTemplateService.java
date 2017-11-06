package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.dao.IPrGroupTemplateMapper;
import com.ciicsh.zorro.leopardwebservice.entity.PrGroupTemplateEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/11/2.
 */
@Service
public class PrGroupTemplateService implements IPrGroupTemplateService {

    @Autowired
    private IPrGroupTemplateMapper prGroupTemplateMapper;

    final static int PAGE_SIZE = 10;

    @Override
    public PageInfo<PrGroupTemplateEntity> getList(PrGroupTemplateEntity param, Integer pageNum) {
        List<PrGroupTemplateEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prGroupTemplateMapper.selectList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrGroupTemplateEntity> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public PrGroupTemplateEntity getItemById(String entityId) {
        PrGroupTemplateEntity result = new PrGroupTemplateEntity();
        try {
            result = prGroupTemplateMapper.selectItemById(entityId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int addItem(PrGroupTemplateEntity param) {
        int result = 0;
        try {
            result = prGroupTemplateMapper.insertItem(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteById(String entityId) {
        int result = 0;
        try {
            result = prGroupTemplateMapper.deleteItemById(entityId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional
    public int updateItemById(PrGroupTemplateEntity param) {

        //TODO 更新薪资项部分
        int result = 0;
        try {
            result = prGroupTemplateMapper.updateItemById(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> getNameList() {
        List<String> resultList = new ArrayList<>();
        try {
            resultList = prGroupTemplateMapper.selectNameList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
