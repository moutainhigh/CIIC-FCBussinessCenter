package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.dao.IPrItemTemplateMapper;
import com.ciicsh.zorro.leopardwebservice.entity.PrItemTemplateEntity;
import com.ciicsh.zorro.leopardwebservice.service.util.ConditionAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/20.
 */
@Service
public class PrItemTemplateService implements IPrItemTemplateService {

    @Autowired
    private IPrItemTemplateMapper prItemTemplateMapper;

    @Autowired
    private CacheManager cacheManager;

    static final Integer PAGE_SIZE = 5;

    @Override
    public List<PrItemTemplateEntity> getList(PrItemTemplateEntity paramItem) {
        List<PrItemTemplateEntity> resultList = new ArrayList<>();
        try {
            resultList = prItemTemplateMapper.selectList(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public PageInfo<PrItemTemplateEntity> getList(PrItemTemplateEntity paramItem, Integer pageNum) {
        List<PrItemTemplateEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prItemTemplateMapper.selectList(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrItemTemplateEntity> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public String addItem(PrItemTemplateEntity paramItem) {
        Cache cache = cacheManager.getCache("payRoll");
        cache.put(paramItem.getCode(), paramItem);
        int result = 0;
        try {
            result = prItemTemplateMapper.insertItem(paramItem);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result == 0 ? "" : paramItem.getCode();
    }

    @Override
    public List<String> getNameList(String managementId) {
        List<String> resultList = new ArrayList<>();
        try {
            resultList = prItemTemplateMapper.selectNameList(managementId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public PrItemTemplateEntity getItem(PrItemTemplateEntity paramItem) {
        PrItemTemplateEntity result = new PrItemTemplateEntity();
        PrItemTemplateEntity item = getPrItemTemplateFromCache(paramItem.getCode());
        if(item != null){
            return item;
        }
        try {
            result = prItemTemplateMapper.selectItem(paramItem);
            result.setResolvedCondition(ConditionAdapter.getConditionStr(result.getCondition()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int updateItem(PrItemTemplateEntity paramItem) {
        int resultCount = 0;
        Cache cache = cacheManager.getCache("payRoll");
        cache.put(paramItem.getCode(), null);
        try {
            resultCount = prItemTemplateMapper.updateItem(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultCount;
    }

    private PrItemTemplateEntity getPrItemTemplateFromCache(String code) {
        Cache cache = cacheManager.getCache("payRoll");
        PrItemTemplateEntity item;
        if (cache.get(code) == null) {
            return null;
        } else {
            item = (PrItemTemplateEntity) cache.get(code).get();
        }
        return item;
    }
}
