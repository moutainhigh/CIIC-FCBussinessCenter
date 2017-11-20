package com.ciicsh.gt1.fcsupportcenter.salarygrantservice.dao;

import com.ciicsh.gt1.fcsupportcenter.businessentity.entity.SsEmpRefund;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by houwanhua on 2017/11/14.
 */
@Mapper
@Component
public interface ISsEmpRefundMapper {
    /**
     * 根据雇员任务单id获取雇员社保退账受理信息
     * @param employeeTaskId 雇员任务单id
     * @return 雇员社保退账受理信息
     * */
    SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId);
}
