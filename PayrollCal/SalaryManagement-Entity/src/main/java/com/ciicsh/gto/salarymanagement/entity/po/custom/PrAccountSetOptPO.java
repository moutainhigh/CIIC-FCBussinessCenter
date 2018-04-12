package com.ciicsh.gto.salarymanagement.entity.po.custom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/20.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrAccountSetOptPO {
    private String managementId;
    private String accountSetName;
    private String accountSetCode;
    List<PrItemInAccountSetPO> accountItems;
}
