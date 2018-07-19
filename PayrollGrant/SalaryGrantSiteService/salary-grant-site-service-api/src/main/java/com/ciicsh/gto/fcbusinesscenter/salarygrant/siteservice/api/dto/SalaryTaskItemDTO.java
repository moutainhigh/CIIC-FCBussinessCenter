package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 薪资发放项
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryTaskItemDTO {
    /**
     * 薪资项code
     */
    private String itemCode;
    /**
     * 薪资项name
     */
    private String itemName;
    /**
     * 薪资项value
     */
    private String itemValue;
}
