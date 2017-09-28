package com.ciicsh.gto.fcsupportcenter.suppliergrantcommandservice.dao;

import com.ciicsh.gto.fcsupportcenter.entity.SsEmpRefund;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by houwanhua on 2017/9/27.
 */
@Mapper
public interface SsEmpRefundMapper {
    /**
     * 根据雇员任务单id获取雇员社保退账受理信息
     * @param employeeTaskId 雇员任务单id
     * @return 雇员社保退账受理信息
     * */
    SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId);
}
