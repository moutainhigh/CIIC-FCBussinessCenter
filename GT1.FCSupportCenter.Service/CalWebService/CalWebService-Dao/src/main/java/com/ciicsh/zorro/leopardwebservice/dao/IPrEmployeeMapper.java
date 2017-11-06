package com.ciicsh.zorro.leopardwebservice.dao;


import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface IPrEmployeeMapper {

    /**
     * 插入雇员列表
     * @param paramList 待雇员列表
     * @return 插入条数
     */
    int insertList( List<PrEmployeeEntity> paramList);

    /**
     * 获取雇员列表
     * @param param 查询参数
     * @return 雇员列表
     */
    List<PrEmployeeEntity> selectList(PrEmployeeEntity param);
}