package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/24.
 */
@Mapper
@Component
public interface IPrGroupMapper {

    /**
     * 获取薪资组列表
     * @param paramItem 查询参数
     * @return 薪资组列表
     */
    List<PrGroupEntity> selectList(PrGroupEntity paramItem);

    /**
     * 获取一个薪资组
     * @param entityId 查询参数
     * @return 薪资组实体
     */
    PrGroupEntity selectItemById(@Param("entityId") String entityId);

    /**
     * 更新一个薪资组
     * @param paramItem 更新参数
     * @return 更新条数
     */
    int updateItem(PrGroupEntity paramItem);

    /**
     * 插入一个薪资组
     * @param paramItem 待插入薪资组实体
     * @return 插入条数
     */
    int insertItem(PrGroupEntity paramItem);

    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @return 薪资组名称列表
     */
    List<String> selectNameList(@Param("managementId") String managementId);

    /**
     *　薪资列表查询
     * @param paramItem
     * @return
     */
    List<PrGroupEntity> selectQueryList(PrGroupEntity paramItem);

    /**
     * 根据模板id获取薪资组列表
     * @param prGroupTemplateId
     * @return
     */
    List<PrGroupEntity> selectListByTemplateId(String prGroupTemplateId);

    /**
     * 删除薪资组
     * @param prGroupId
     * @return
     */
    int deletePrGroup(@Param("prGroupId") String prGroupId);
}
