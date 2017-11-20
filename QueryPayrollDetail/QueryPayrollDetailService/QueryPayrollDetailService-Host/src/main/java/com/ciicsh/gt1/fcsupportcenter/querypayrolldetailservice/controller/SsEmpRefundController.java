package com.ciicsh.gt1.fcsupportcenter.querypayrolldetailservice.controller;


import com.ciicsh.gt1.fcsupportcenter.businessentity.entity.SsEmpRefund;
import com.ciicsh.gt1.fcsupportcenter.querypayrolldetailservice.business.ISsEmpRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by houwanhua on 2017/11/20.
 */
@RestController
public class SsEmpRefundController {
    @Autowired
    private ISsEmpRefundService ssEmpRefundService;

    /**
     * 根据雇员任务单id获取雇员社保退账受理信息
     * @param employeeTaskId 雇员任务单id
     * @return 雇员社保退账受理信息
     * */
    @RequestMapping(value = "/getSsEmpRefundByEmployeeTaskId")
    public SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId){
        return ssEmpRefundService.getSsEmpRefundByEmployeeTaskId(employeeTaskId);
    }
}
