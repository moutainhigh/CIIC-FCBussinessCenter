package com.ciicsh.zorro.leopardwebservice.dao;

import com.ciicsh.zorro.leopardwebservice.entity.PrGroupTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/2.
 */
@Mapper
@Component
public interface IPrGroupTemplateMapper {

    /**
     * 获取薪资项模板列表
     * @param param 查询参数
     * @return 薪资项模板列表
     */
    List<PrGroupTemplateEntity> selectList(PrGroupTemplateEntity param);

    /**
     * 获取一个薪资项模板
     * @param entityId entityId
     * @return 薪资项模板
     */
    PrGroupTemplateEntity selectItemById(@Param("entityId") String entityId);

    /**
     * 插入一个薪资组模板
     * @param param 待插入薪资组模板entity
     * @return 插入条数
     */
    int insertItem(PrGroupTemplateEntity param);

    /**
     * 删除薪资组模板 BY entityId
     * @param entityId entityId
     * @return 删除条数
     */
    int deleteItemById(@Param("entityId") String entityId);

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
    List<String> selectNameList();
}
