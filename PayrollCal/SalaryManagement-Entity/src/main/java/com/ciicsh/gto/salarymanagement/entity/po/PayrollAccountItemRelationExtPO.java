package com.ciicsh.gto.salarymanagement.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by houwanhua on 2017/12/20.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PayrollAccountItemRelationExtPO {
    private Integer id;
    private String accountSetCode;
    private String payrollItemCode;
    private String payrollItemName;
    private Integer payrollItemType;
    private Integer payrollDataType;
    private String payrollItemAlias;
    private Boolean isActive;
}
