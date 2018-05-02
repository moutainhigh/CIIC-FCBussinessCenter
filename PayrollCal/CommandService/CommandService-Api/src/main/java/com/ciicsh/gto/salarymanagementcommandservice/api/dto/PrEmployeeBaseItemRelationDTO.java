package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 固定项表。记录固定项的名字和值，和雇员雇员ID关联和批次无关。
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
public class PrEmployeeBaseItemRelationDTO{

    private static final long serialVersionUID = 1L;

    /**
     * 正常批次计算结果ID
     */
	private Long id;
    /**
     * 雇员ID
     */
	private String empId;
    /**
     * 薪资项计算结果
     */
	private String empBaseValue;
    /**
     * 基类薪资项Code
     */
	private String baseItemCode;
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
