package com.ciicsh.gt1.fcsupportcenter.bounce.controller;


import com.ciicsh.gt1.fcsupportcenter.businessentity.entity.SsEmpRefund;
import com.ciicsh.gt1.fcsupportcenter.bounce.business.ISsEmpRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by?houwanhua?on?2017/11/8.
 */
@RestController
public class SsEmpRefundController {
    @Autowired
    private ISsEmpRefundService ssEmpRefundService;

    /**
     * ���ݹ�Ա����id��ȡ��Ա�籣����������Ϣ
     * @param employeeTaskId ��Ա����id
     * @return ��Ա�籣����������Ϣ
     * */
    @RequestMapping(value = "/getSsEmpRefundByEmployeeTaskId")
    public SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId){
        return ssEmpRefundService.getSsEmpRefundByEmployeeTaskId(employeeTaskId);
    }
}
