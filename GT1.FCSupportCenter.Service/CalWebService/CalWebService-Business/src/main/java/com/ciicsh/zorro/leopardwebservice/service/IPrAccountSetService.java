package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.entity.PrAccountSetEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/1.
 */
public interface IPrAccountSetService {

    /**
     * 插入一个薪资账套
     * @param param 待插入薪资账套
     * @return 插入条数
     */
    int addItem(PrAccountSetEntity param);

    /**
     * 删除一个薪资账套
     * @param param 待删除的薪资账套
     * @return 删除条数
     */
    int deleteItemById(PrAccountSetEntity param);

    /**
     * 获取薪资账套列表
     * @param param 查询参数
     * @param pageNum 页码
     * @return 薪资账套列表
     */
    PageInfo<PrAccountSetEntity> getList(PrAccountSetEntity param, Integer pageNum);

    /**
     * 获取薪资账套 By Id
     * @param param 查询参数
     * @return 薪资账套
     */
    PrAccountSetEntity getItemById(String id);

    /**
     * 更新一个薪资账套
     * @param param 更新参数
     * @return 更新条数
     */
    int updateItemById(PrAccountSetEntity param);

    /**
     * 获取薪资账套名称列表
     * @param managementId 管理方id
     * @return 薪资账套名称列表
     */
    List<String> getNameList(String managementId);
}
