package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 薪资账套薪资项名表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollAccountItemRelationDTO{

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	private Long id;
    /**
     * 薪酬账套代码
     */
	private String accountSetCode;
    /**
     * 薪资项编码, 规则为XZX-9位管理方ID-薪资项类型缩写-3位数字序号
     */
	private String payrollItemCode;
    /**
     * 薪资账套薪资项别名
     */
	private String payrollItemAlias;
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
