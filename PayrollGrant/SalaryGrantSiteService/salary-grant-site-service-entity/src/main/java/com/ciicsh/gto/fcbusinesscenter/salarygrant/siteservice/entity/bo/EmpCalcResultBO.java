package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * <p>Description: 雇员薪资项结果 BO</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/18 0018
 */
@Data
public class EmpCalcResultBO {
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 发放币种:CNY-人民币，USD-美元，EUR-欧元
     */
    private String currencyCode;
    /**
     * 薪资项结果列表
     */
    List<CalcResultItemBO> empCalcResultItemsList;
}
