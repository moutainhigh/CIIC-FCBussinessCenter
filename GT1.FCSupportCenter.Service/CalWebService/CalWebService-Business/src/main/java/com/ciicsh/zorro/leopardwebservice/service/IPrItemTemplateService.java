package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.entity.PrItemTemplateEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/20.
 */
public interface IPrItemTemplateService {

    /**
     * 获取薪资项模板列表
     * @param paramItem 查询参数
     * @return 结果列表
     */
    List<PrItemTemplateEntity> getList(PrItemTemplateEntity paramItem);

    /**
     * 获取薪资项模板列表(分页)
     * @param paramItem 查询参数
     * @return 结果列表
     */
    PageInfo<PrItemTemplateEntity> getList(PrItemTemplateEntity paramItem, Integer pageNum);

    /**
     * 添加薪资项模板列表
     * @param paramItem 薪资项模板实体
     * @return 薪资项模板编码
     */
    String addItem(PrItemTemplateEntity paramItem);

    /**
     * 获取薪资项模板名称列表
     * @param managementId 管理方ID
     * @param companyId 客户ID
     * @return
     */
    List<String> getNameList(String managementId);

    /**
     * 获取一个薪资项模板
     * @param paramItem 查询参数
     * @return 结果项
     */
    PrItemTemplateEntity getItem(PrItemTemplateEntity paramItem);

    /**
     * 更新一个薪资项模板
     * @param paramItem 更新薪资项模板实体
     * @return 更新条数
     */
    int updateItem(PrItemTemplateEntity paramItem);
}
