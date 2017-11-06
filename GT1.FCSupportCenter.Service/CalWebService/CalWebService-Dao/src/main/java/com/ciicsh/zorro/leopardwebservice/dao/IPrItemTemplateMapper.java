package com.ciicsh.zorro.leopardwebservice.dao;

import com.ciicsh.zorro.leopardwebservice.entity.PrItemTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/20.
 */
@Mapper
@Component
public interface IPrItemTemplateMapper {

    /**
     * 获取薪资项模板列表
     * @param prItemTemplate 查询参数
     * @return 结果列表
     */
    List<PrItemTemplateEntity> selectList(PrItemTemplateEntity prItemTemplate);

    /**
     * 获取一个薪资项模板
     * @param prItemTemplate 查询参数
     * @return 结果项
     */
    PrItemTemplateEntity selectItem(PrItemTemplateEntity prItemTemplate);

    /**
     * 插入一个薪资项模板
     * @param prItemTemplate 待插入实体
     * @return 插入结果
     */
    int insertItem(PrItemTemplateEntity prItemTemplate);

    /**
     * 获取薪资项模板名称列表
     * @param managementId 管理方ID
     * @param companyId 客户ID
     * @return 名称列表
     */
    List<String> selectNameList(@Param("managementId") String managementId);

    /**
     * 更新一个薪资项模板
     * @param prItemTemplate 更新薪资项模板实体
     * @return 更新条数
     */
    int updateItem(PrItemTemplateEntity prItemTemplate);
}
