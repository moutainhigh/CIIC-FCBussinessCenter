package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeGroupEntity;
import com.ciicsh.zorro.leopardwebservice.entity.PrItemTemplateEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/25.
 */
public interface IPrEmployeeGroupService {

    /**
     * 获取一个雇员组
     * @param paramItem 参数实体
     * @return 结果雇员组
     */
    PrEmployeeGroupEntity getItem(PrEmployeeGroupEntity paramItem);

    /**
     * 获取雇员组列表
     * @param paramItem 参数实体
     * @param pageNum 页码
     * @return 结果雇员组列表
     */
    PageInfo<PrEmployeeGroupEntity> getList(PrEmployeeGroupEntity paramItem, Integer pageNum);

    /**
     * 插入一个雇员组
     * @param paramEntity 待插入实体
     * @return 插入条数
     */
    int addItem(PrEmployeeGroupEntity paramEntity);

    /**
     * 更新一个雇员组
     * @param paramEntity 更新参数
     * @return 更新条数
     */
    int updateItem(PrEmployeeGroupEntity paramEntity);

    /**
     * 获取雇员组名称列表
     * @param paramEntity 参数实体
     * @return 名称列表
     */
    List<String> getNameList(String managementId, String companyId);
}
