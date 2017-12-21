package com.ciicsh.gto.salarymanagement.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by houwanhua on 2017/12/19.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PayrollGroupExtPO {
    private String managementId;
    private String payrollGroupCode;
    private String payrollGroupTemplateCode;
}
