package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 薪资账套薪资项别名表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollAccountItemaliasDTO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	private Long id;
    /**
     * 薪资账套Id
     */
	private Integer payrollAccountId;
    /**
     * 薪资项ID
     */
	private Integer payrollItemId;
    /**
     * 薪资账套薪资项别名
     */
	private String payrollItemAlias;
    /**
     * 是否有效
     */
	private Boolean isActive;
    /**
     * 是否显示
     */
	private Boolean isShow;
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
