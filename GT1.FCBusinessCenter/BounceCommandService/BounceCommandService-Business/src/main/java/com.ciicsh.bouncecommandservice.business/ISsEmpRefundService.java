package com.ciicsh.bouncecommandservice.business;

import com.ciicsh.bouncecommandservice.entity.SsEmpRefund;
import org.springframework.stereotype.Service;

/**
 * Created by houwanhua on 2017/11/8.
 */
@Service
public interface ISsEmpRefundService {
    SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId);
}
