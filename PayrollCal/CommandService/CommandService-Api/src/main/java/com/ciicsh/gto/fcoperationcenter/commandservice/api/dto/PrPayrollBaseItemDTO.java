package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollBaseItemDTO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	private Integer id;
    /**
     * 薪资项名称
     */
	private String baseItemName;
    /**
     * 基类薪资项Code
     */
	private String baseItemCode;
    /**
     * 薪资项类型：
1 - 固定输入项，一次初始化，很少变更，建立批次时一般直接使用 
     */
	private Integer baseItemType;
    /**
     * 数据格式: 1-文本,2-数字,3-日期,4-布尔
     */
	private Integer dataType;
    /**
     * 默认值处理方式：
1 - 使用历史数据 
2 - 使用基本值
     */
	private Integer defaultValueStyle;
    /**
     * 默认数字
     */
	private String defaultValue;
    /**
     * 小数处理方式：
1 - 四舍五入 
2 - 简单去位
     */
	private Integer decimalProcessType;
    /**
     * 计算精度
     */
	private Integer calPrecision;
    /**
     * 薪资项取值范围的条件描述
     */
	private String baseItemCondition;
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
