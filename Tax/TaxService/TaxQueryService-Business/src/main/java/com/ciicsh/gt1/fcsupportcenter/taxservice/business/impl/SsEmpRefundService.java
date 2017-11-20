package com.ciicsh.gt1.fcsupportcenter.taxservice.business.impl;


import com.ciicsh.gt1.fcsupportcenter.businessentity.entity.SsEmpRefund;
import com.ciicsh.gt1.fcsupportcenter.taxservice.business.ISsEmpRefundService;
import com.ciicsh.gt1.fcsupportcenter.taxservice.dao.ISsEmpRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by houwanhua on 2017/11/8.
 */
@Service
@Transactional
public class SsEmpRefundService implements ISsEmpRefundService {
    @Autowired
    private ISsEmpRefundMapper ssEmpRefundMapper;

    /**
     * 根据雇员任务单id获取雇员社保退账受理信息
     * @param employeeTaskId 雇员任务单id
     * @return 雇员社保退账受理信息
     * */
    public SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId){
        return ssEmpRefundMapper.getSsEmpRefundByEmployeeTaskId(employeeTaskId);
    }
}
