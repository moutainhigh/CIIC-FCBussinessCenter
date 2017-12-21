package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by houwanhua on 2017/12/20.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PayrollAccountItemRelationExtDTO {
    private Integer id;
    private String accountSetCode;
    private String payrollItemCode;
    private String payrollItemName;
    private Integer payrollItemType;
    private Integer payrollDataType;
    private String payrollItemAlias;
    private Boolean isActive;
}
