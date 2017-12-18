package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/6.
 */
@Mapper
@Component
public interface IPrItemMapper {

    /**
     * 获取薪资项列表
     * @return
     */
    List<PrItemEntity> selectList();

    /**
     * 查询薪资项列表
     * @param param 查询参数
     * @return 结果列表
     */
    List<PrItemEntity> selectQueryList(PrItemEntity param);

    /**
     * 获取一个薪资项
     * @param id 查询参数
     * @return 结果项
     */
    PrItemEntity selectItemById(@Param("entityId") String id);

    /**
     * 插入一个薪资项
     * @param param 待插入实体
     * @return 插入结果
     */
    int insertItem(PrItemEntity param);

    /**
     * 插入一组薪资项
     * @param paramList 待插入列表
     * @return 插入条数
     */
    int insertList(List<PrItemEntity> paramList);

    /**
     * 获取薪资项名称列表
     * @param managementId 管理方ID
     * @return 名称列表
     */
    List<String> selectNameList(@Param("managementId") String managementId);

    /**
     * 更新一个薪资项模板
     * @param param 更新薪资项实体
     * @return 更新条数
     */
    int updateItemById(PrItemEntity param);

    /**
     * 获取薪资项类型列表
     * @param managementId
     * @return
     */
    List<Integer> selectTypeList(@Param("managementId") String managementId);

    /**
     * 根据薪资组模板id获取薪资项列表
     * @param groupTemplateId
     * @return
     */
    List<PrItemEntity> selectListByGroupTemplateId(@Param("prGroupTemplateId") String groupTemplateId);

    /**
     * 根据薪资组id获取薪资项列表
     * @param groupId
     * @return
     */
    List<PrItemEntity> selectListByGroupId(@Param("prGroupId") String groupId);

    /**
     * 删除薪资项 BY ID
     * @param entityId
     * @return
     */
    int deleteItemById(@Param("entityId") String entityId);


    /**
     * 获取管理方下不存在某薪资项的
     * @param managementId
     * @return
     */
    List<String> selectGroupIdsWithoutItemName(@Param("managementId") String managementId,
                                               @Param("name") String prItemName);

    /**
     * 删除一个薪资组下的所有薪资项 BY prGroupId
     * @param prGroupId
     * @return
     */
    int deleteItemByPrGroupId(@Param("groupId") String prGroupId);
}
