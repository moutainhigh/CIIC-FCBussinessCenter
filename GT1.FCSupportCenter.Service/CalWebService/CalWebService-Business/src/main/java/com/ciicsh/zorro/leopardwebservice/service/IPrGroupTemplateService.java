package com.ciicsh.zorro.leopardwebservice.service;

import com.ciicsh.zorro.leopardwebservice.entity.PrGroupTemplateEntity;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/2.
 */
public interface IPrGroupTemplateService {

    /**
     * 获取薪资项模板列表
     * @param param 查询参数
     * @return 薪资项模板列表
     */
    PageInfo<PrGroupTemplateEntity> getList(PrGroupTemplateEntity param, Integer pageNum);

    /**
     * 获取一个薪资项模板
     * @param entityId entityId
     * @return 薪资项模板
     */
    PrGroupTemplateEntity getItemById(String entityId);

    /**
     * 插入一个薪资组模板
     * @param param 待插入薪资组模板entity
     * @return 插入条数
     */
    int addItem(PrGroupTemplateEntity param);

    /**
     * 删除薪资组模板 BY entityId
     * @param entityId entityId
     * @return 删除条数
     */
    int deleteById(String entityId);

    /**
     * 更新薪资组模板 BY entityId
     * @param param 更新参数
     * @return 更新条数
     */
    int updateItemById(PrGroupTemplateEntity param);

    /**
     * 获取薪资组模板名称列表
     * @return 薪资组模板名称列表
     */
    List<String> getNameList();
}
