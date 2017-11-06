package com.ciicsh.zorro.leopardwebservice.dao;

import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeEntity;
import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/25.
 */
@Mapper
@Component
public interface IPrEmployeeGroupMapper {

    /**
     * 获取一个雇员组
     * @param paramItem 参数实体
     * @return 结果雇员组
     */
    PrEmployeeGroupEntity selectItemById(PrEmployeeGroupEntity paramItem);

    /**
     * 获取雇员组列表
     * @param paramItem 参数实体
     * @return 结果雇员组列表
     */
    List<PrEmployeeGroupEntity> selectList(PrEmployeeGroupEntity paramItem);

    /**
     * 插入一个雇员组
     * @param paramEntity 待插入实体
     * @return 插入条数
     */
    int insertItem(PrEmployeeGroupEntity paramEntity);

    /**
     * 更新一个雇员组
     * @param paramEntity 更新参数
     * @return 更新条数
     */
    int updateItemById(PrEmployeeGroupEntity paramEntity);

    /**
     * 获取雇员组名称列表
     * @param managementId 管理方
     * @param companyId 客户
     * @return 名称列表
     */
    List<String> selectNameList(@Param("managementId") String managementId, @Param("companyId") String companyId);

    /**
     * 插入雇员组-雇员关系列表
     * @param paramEntity 待插入雇员组
     * @return 插入条数
     */
    int insertRelationList(PrEmployeeGroupEntity paramEntity);

    /**
     * 删除雇员组-雇员关系列表 By EmpGroupId
     * @param paramEntity
     * @return
     */
    int deleteRelationByEmpGroupId(@Param("empGroupId") String prEmployeeGroupId);
}
