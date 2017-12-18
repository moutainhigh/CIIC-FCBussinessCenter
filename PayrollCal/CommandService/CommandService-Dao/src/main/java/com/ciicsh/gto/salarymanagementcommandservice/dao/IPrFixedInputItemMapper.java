package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.ciicsh.gto.salarymanagement.entity.PrFixedInputItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/14.
 */
@Mapper
@Component
public interface IPrFixedInputItemMapper {


    List<PrFixedInputItemEntity> selectList();

    /**
     * 插入导入雇员固定薪资项
     * @param paramList
     * @return
     */
    int insertList(@Param("paramList") List<PrFixedInputItemEntity> paramList);

    /**
     * 更具ID删除一批数据
     * @param empIds
     * @return
     */
    int deleteListByEmployeeId(@Param("empIds") List<String> empIds);
}
