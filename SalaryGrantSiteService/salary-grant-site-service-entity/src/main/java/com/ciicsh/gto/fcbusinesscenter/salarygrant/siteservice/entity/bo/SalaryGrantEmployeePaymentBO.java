package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import lombok.Data;
import lombok.ToString;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/25 0025
 */
@Data
@ToString(callSuper = true)
public class SalaryGrantEmployeePaymentBO extends SalaryGrantEmployeePO {
    /**
     * 是否要付个税(1:是；0:否)
     */
    private Integer ifTaxPay;
    /**
     * 是否要付工资(1:是；0:否)
     */
    private Integer ifSalaryPay;
    /**
     * 雇员明细状态雇员个税状态(0:正常;1:暂缓放开;负值标识未完成/未放开)
     */
    private Integer taxStatus;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 城市名
     */
    private String cityName;

}
