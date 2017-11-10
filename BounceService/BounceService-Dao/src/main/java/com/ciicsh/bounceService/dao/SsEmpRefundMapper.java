package com.ciicsh.bounceService.dao;

import com.ciicsh.business.entity.SsEmpRefund;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by houwanhua on 2017/11/8.
 */
@Mapper
@Component
public interface SsEmpRefundMapper {
    /**
     * 根据雇员任务单id获取雇员社保退账受理信息
     * @param employeeTaskId 雇员任务单id
     * @return 雇员社保退账受理信息
     * */
    SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId);
}
