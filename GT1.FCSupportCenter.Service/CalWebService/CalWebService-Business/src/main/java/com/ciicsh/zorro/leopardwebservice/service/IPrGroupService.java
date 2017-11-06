package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.entity.PrGroupEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/24.
 */
public interface IPrGroupService {

    /**
     * 获取薪资组列表
     * @param paramItem 查询参数
     * @return 薪资组列表
     */
    PageInfo<PrGroupEntity> getList(PrGroupEntity paramItem, Integer pageNum);

    /**
     * 获取一个薪资组
     * @param paramItem 查询参数
     * @return 薪资组实体
     */
    PrGroupEntity getItem(PrGroupEntity paramItem);

    /**
     * 更新一个薪资组
     * @param paramItem 更新参数
     * @return 更新条数
     */
    int updateItem(PrGroupEntity paramItem);

    /**
     * 插入一个薪资组
     * @param paramItem 待插入薪资组实体
     * @return 薪资组code
     */
    String addItem(PrGroupEntity paramItem);

    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @param companyId 客户ID
     * @return 薪资组名称列表
     */
    List<String> getNameList(String managementId);

    /**
     *
     * @param paramItem
     * @param pageNum
     * @return
     */
    PageInfo<PrGroupEntity> getQueryList(PrGroupEntity paramItem, Integer pageNum);
}
