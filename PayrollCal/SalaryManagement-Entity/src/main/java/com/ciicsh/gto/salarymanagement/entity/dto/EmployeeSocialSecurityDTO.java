package com.ciicsh.gto.salarymanagement.entity.dto;

import com.ciicsh.gto.salarymanagement.entity.PrBaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by jiangtianning on 2017/11/14.
 */
@Data
public class EmployeeSocialSecurityDTO extends PrBaseEntity {

    private Integer fixedInputItemId;

    private String managementId;

    private String employeeId;

    private String prItemName;

    private BigDecimal prItemValue;
}
