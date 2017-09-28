package com.ciicsh.gto.fcsupportcenter.bouncequeryservice.controller;

import com.ciicsh.gto.fcsupportcenter.bouncequeryservice.business.PrPayRollService;
import com.ciicsh.gto.fcsupportcenter.entity.PrPayRoll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by houwanhua on 2017/9/27.
 */
@RestController
public class PrPayRollController {
    @Autowired
    private PrPayRollService prPayRollService;

    /**
     * 根据雇员id获取工资单信息
     * @param employeeId 雇员id
     * @return 工资单信息
     * */
    @RequestMapping(value = "/getParRollByEmployeeId")
    public PrPayRoll getParRollByEmployeeId(String employeeId){
        return prPayRollService.getParRollByEmployeeId(employeeId);
    }
}
