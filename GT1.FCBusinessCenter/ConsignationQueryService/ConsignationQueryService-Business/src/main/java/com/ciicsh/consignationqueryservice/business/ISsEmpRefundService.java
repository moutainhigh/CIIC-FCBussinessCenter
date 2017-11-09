package com.ciicsh.consignationqueryservice.business;

import com.ciicsh.consignationqueryservice.entity.SsEmpRefund;
import org.springframework.stereotype.Service;

/**
 * Created by houwanhua on 2017/11/9.
 */
@Service
public interface ISsEmpRefundService {
    SsEmpRefund getSsEmpRefundByEmployeeTaskId(String employeeTaskId);
}