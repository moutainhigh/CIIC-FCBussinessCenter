package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.Data;

/**
 * <p>Description: 雇员薪资项 BO</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/17 0017
 */
@Data
public class CalcResultItemBO {
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
