package com.ciicsh.zorro.leopardwebservice.dao;

import com.ciicsh.zorro.leopardwebservice.entity.PrAccountSetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/1.
 */
@Mapper
@Component
public interface IPrAccountSetMapper {

    /**
     * 插入一个薪资账套
     * @param param 待插入薪资账套
     * @return 插入条数
     */
    int insertItem(PrAccountSetEntity param);

    /**
     * 删除一个薪资账套
     * @param param 待删除的薪资账套
     * @return 删除条数
     */
    int deleteItemById(PrAccountSetEntity param);

    /**
     * 获取薪资账套列表
     * @param param 查询参数
     * @return 薪资账套列表
     */
    List<PrAccountSetEntity> selectList(PrAccountSetEntity param);

    /**
     * 获取薪资账套 By entityId
     * @param entityId 查询参数
     * @return 薪资账套
     */
    PrAccountSetEntity selectItemById(@Param("entityId") String entityId);

    /**
     * 更新一个薪资账套
     * @param param 更新参数
     * @return 更新条数
     */
    int updateItemById(PrAccountSetEntity param);

    /**
     * 获取薪资账套名称列表 By managementId
     * @param managementId
     * @return 薪资账套名称列表
     */
    List<String> selectNameList(@Param("managementId") String managementId);
}
