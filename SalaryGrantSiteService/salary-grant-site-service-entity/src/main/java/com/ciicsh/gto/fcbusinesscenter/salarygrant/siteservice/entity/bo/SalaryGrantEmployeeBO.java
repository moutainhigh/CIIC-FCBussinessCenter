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
@Data
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantEmployeeBO extends SalaryGrantEmployeePO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务大类名称
     */
    private String templateTypeName;
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

    /**
     * 扣个人所得税（中智公司）
     */
    private BigDecimal taxAF;

    /**
     * 扣个人所得税（财务公司-FC）
     */
    private BigDecimal taxFC;

    /**
     * 扣个人所得税（外服公司-BPO）
     */
    private BigDecimal taxBPO;

    /**
     * 扣个人所得税（独立库）
     */
    private BigDecimal taxIndependence;
    /**
     * 任务单状态
     */
    private String taskStatus;
    /**
     * 任务单ID
     */
    private Long taskId;
    /**
     * 调整信息
     */
    private String adjustInfo;

}
