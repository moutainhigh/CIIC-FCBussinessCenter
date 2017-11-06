package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.dao.IPrEmployeeGroupMapper;
import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeGroupEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/25.
 */
@Service
public class PrEmployeeGroupService implements IPrEmployeeGroupService {

    @Autowired
    private IPrEmployeeGroupMapper prEmployeeGroupMapper;

    final static int PAGE_SIZE = 5;

    @Override
    public PrEmployeeGroupEntity getItem(PrEmployeeGroupEntity paramItem) {

        PrEmployeeGroupEntity result = new PrEmployeeGroupEntity();
        try {
            result = prEmployeeGroupMapper.selectItemById(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<PrEmployeeGroupEntity> getList(PrEmployeeGroupEntity paramItem, Integer pageNum) {

        List<PrEmployeeGroupEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prEmployeeGroupMapper.selectList(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrEmployeeGroupEntity> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    @Transactional
    public int addItem(PrEmployeeGroupEntity paramEntity) {

        //雇员组插入结果
        int insertPrEmpGroupResult = 0;
        //雇员组-雇员关系列表插入结果
        int insertPrEmpResult = 0;
        try {
            insertPrEmpGroupResult = prEmployeeGroupMapper.insertItem(paramEntity);
            if (insertPrEmpGroupResult == 1) {
                insertPrEmpResult = prEmployeeGroupMapper.insertRelationList(paramEntity);
            } else {
                //TODO 雇员组插入失败处理
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (insertPrEmpResult == 0) {
            //TODO 雇员组-雇员关系表插入失败处理
            return 0;
        }
        return insertPrEmpGroupResult;
    }

    @Override
    @Transactional
    public int updateItem(PrEmployeeGroupEntity paramEntity) {

        //雇员组更新结果
        int updatePrEmpGroupResult = 0;
        //雇员组-雇员关系列表插入结果
        int insertPrEmpResult = 0;
        try {
            updatePrEmpGroupResult = prEmployeeGroupMapper.updateItemById(paramEntity);
            if (updatePrEmpGroupResult == 1 && paramEntity.getEmployeeList() != null) {
                prEmployeeGroupMapper.deleteRelationByEmpGroupId(paramEntity.getEntityId());
                insertPrEmpResult = prEmployeeGroupMapper.insertRelationList(paramEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (insertPrEmpResult == 0) {
            //TODO 雇员组-雇员关系表插入失败处理
            return 0;
        }
        return updatePrEmpGroupResult;
    }

    @Override
    public List<String> getNameList(String managementId, String companyId) {

        List<String> resultList = new ArrayList<>();
        try {
            resultList = prEmployeeGroupMapper.selectNameList(managementId, companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
