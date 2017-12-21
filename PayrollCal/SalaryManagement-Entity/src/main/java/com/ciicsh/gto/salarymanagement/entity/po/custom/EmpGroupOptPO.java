package com.ciicsh.gto.salarymanagement.entity.po.custom;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by houwanhua on 2017/12/20.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmpGroupOptPO {
    private String managementId;
    private String empGroupName;
    private String empGroupCode;
}
