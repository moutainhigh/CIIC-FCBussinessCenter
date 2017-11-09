package com.ciicsh.consignationcommandservice.business;

import com.ciicsh.consignationcommandservice.entity.SsEmpRefund;
import org.springframework.stereotype.Service;

/**
 * Created by houwanhua on 2017/11/9.
 */
@Service
public interface ISsEmpRefundService {
    SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId);
}