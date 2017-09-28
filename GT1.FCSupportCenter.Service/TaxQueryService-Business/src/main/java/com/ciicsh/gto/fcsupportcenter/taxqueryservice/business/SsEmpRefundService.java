package com.ciicsh.gto.fcsupportcenter.taxqueryservice.business;

import com.ciicsh.gto.fcsupportcenter.entity.SsEmpRefund;
import com.ciicsh.gto.fcsupportcenter.taxqueryservice.dao.SsEmpRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by houwanhua on 2017/9/28.
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
