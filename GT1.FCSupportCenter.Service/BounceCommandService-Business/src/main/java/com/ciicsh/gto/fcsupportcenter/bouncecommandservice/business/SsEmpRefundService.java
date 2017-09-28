package com.ciicsh.gto.fcsupportcenter.bouncecommandservice.business;

import com.ciicsh.gto.fcsupportcenter.bouncecommandservice.dao.SsEmpRefundMapper;
import com.ciicsh.gto.fcsupportcenter.entity.SsEmpRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by houwanhua on 2017/9/27.
 */
@Service
@Transactional
public class SsEmpRefundService {
    @Autowired
    private SsEmpRefundMapper ssEmpRefundMapper;

    /**
     * 根据雇员任务单id获取雇员社保退账受理信息
     * @param employeeTaskId 雇员任务单id
     * @return 雇员社保退账受理信息
     * */
    public SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId){
        return ssEmpRefundMapper.getSsEmpRefundByEmployeeTaskId(employeeTaskId);
    }
}
