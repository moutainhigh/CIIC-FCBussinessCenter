package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放雇员信息表
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-02
 */
@ToString(callSuper = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantEmployeeBO extends SalaryGrantEmployeePO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务大类名称
     */
    private String templateTypeName;
    /**
     * 发放账户名称
     */
    private String grantAccountName;
    /**
     * 发放方式名称
     */
    private String grantModeName;
    /**
     * 发放币种名称
     */
    private String currencyName;
    /**
     * 国籍中文
     */
    private String countryName;
    /**
     * 发放金额折合人民币
     */
    private BigDecimal paymentAmountForRMB;
    /**
     * 薪资发放时段中文
     */
    private String grantTimeName;
    /**
     * 发放状态中文
     */
    private String grantStatusName;
    /**
     * 暂缓类型名称
     */
    private String reprieveTypeName;

    /**
     * 任务单类型
     */
    private Integer taskType;

    /**
     * 任务单编号
     */
    private String taskCode;

}
