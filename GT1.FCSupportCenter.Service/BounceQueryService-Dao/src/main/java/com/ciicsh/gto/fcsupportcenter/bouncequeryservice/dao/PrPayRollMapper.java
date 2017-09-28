package com.ciicsh.gto.fcsupportcenter.bouncequeryservice.dao;

import com.ciicsh.gto.fcsupportcenter.entity.PrPayRoll;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by houwanhua on 2017/9/27.
 */
@Mapper
public interface PrPayRollMapper {
    /**
     * 根据雇员id获取工资单信息
     * @param employeeId 雇员id
     * @return 工资单信息
     * */
    PrPayRoll getParRollByEmployeeId(String employeeId);
}
