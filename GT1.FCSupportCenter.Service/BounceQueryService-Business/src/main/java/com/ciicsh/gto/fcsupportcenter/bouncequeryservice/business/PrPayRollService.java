package com.ciicsh.gto.fcsupportcenter.bouncequeryservice.business;

import com.ciicsh.gto.fcsupportcenter.bouncequeryservice.dao.PrPayRollMapper;
import com.ciicsh.gto.fcsupportcenter.entity.PrPayRoll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by houwanhua on 2017/9/27.
 */

@Service
@Transactional
public class PrPayRollService {
    @Autowired
    private PrPayRollMapper prPayRollMapper;

    /**
     * 根据雇员id获取工资单信息
     * @param employeeId 雇员id
     * @return 工资单信息
     * */
    public PrPayRoll getParRollByEmployeeId(String employeeId){
        return prPayRollMapper.getParRollByEmployeeId(employeeId);
    }
}
