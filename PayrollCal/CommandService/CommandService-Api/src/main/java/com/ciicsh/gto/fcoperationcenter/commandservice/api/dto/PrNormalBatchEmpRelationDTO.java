package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 正常批次和雇员关系表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrNormalBatchEmpRelationDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
	private Long id;
    /**
     * 正常批次ID
     */
	private Integer normalBatchId;
    /**
     * 雇员ID
     */
	private String empId;
    /**
     * 当前批次中此雇员的实发工资
     */
	private BigDecimal netPay;
    /**
     * 当前批次中此雇员的个调税金额
     */
	private BigDecimal incomeTax;
    /**
     * 当前批次中，此雇员的实发工资调整金额
     */
	private BigDecimal netPayAdjustAmount;
    /**
     * 当前批次中，此雇员的个调税调整金额
     */
	private BigDecimal incomeTaxAdjustAmount;
    /**
     * 当前批次中，此雇员的实发工资回溯调整金额
     */
	private BigDecimal netPayBadjustAmount;
    /**
     * 是否有效
     */
	private Boolean isActive;
    /**
     * 数据创建时间
     */
	private Date createdTime;
    /**
     * 最后修改时间
     */
	private Date modifiedTime;
    /**
     * 创建人
     */
	private String createdBy;
    /**
     * 最后修改人
     */
	private String modifiedBy;

}
