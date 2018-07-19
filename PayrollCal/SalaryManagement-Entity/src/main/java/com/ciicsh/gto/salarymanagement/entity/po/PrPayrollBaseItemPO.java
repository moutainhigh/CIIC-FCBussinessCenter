package com.ciicsh.gto.salarymanagement.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_payroll_base_item")
public class PrPayrollBaseItemPO extends Model<PrPayrollBaseItemPO> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 薪资项名称
     */
	@TableField("base_item_name")
	private String baseItemName;
    /**
     * 基类薪资项Code
     */
	@TableField("base_item_code")
	private String baseItemCode;
    /**
     * 薪资项类型：
1 - 固定输入项，一次初始化，很少变更，建立批次时一般直接使用 
     */
	@TableField("base_item_type")
	private Integer baseItemType;
    /**
     * 数据格式: 1-文本,2-数字,3-日期,4-布尔
     */
	@TableField("data_type")
	private Integer dataType;
    /**
     * 默认值处理方式：
1 - 使用历史数据 
2 - 使用基本值
     */
	@TableField("default_value_style")
	private Integer defaultValueStyle;
    /**
     * 默认数字
     */
	@TableField("default_value")
	private String defaultValue;
    /**
     * 小数处理方式：
1 - 四舍五入 
2 - 简单去位
     */
	@TableField("decimal_process_type")
	private Integer decimalProcessType;
    /**
     * 计算精度
     */
	@TableField("cal_precision")
	private Integer calPrecision;
    /**
     * 薪资项取值范围的条件描述
     */
	@TableField("base_item_condition")
	private String baseItemCondition;
    /**
     * 是否有效
     */
	@TableField("is_active")
	private Boolean isActive;
    /**
     * 数据创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 最后修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;
    /**
     * 创建人
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 最后修改人
     */
	@TableField("modified_by")
	private String modifiedBy;


	/**
	 * 薪资项显示顺序(该属性不属于数据库表字段，但排序时要用到，故exist=false)
	 */
	@TableField(exist = false)
	private Integer displayPriority;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBaseItemName() {
		return baseItemName;
	}

	public void setBaseItemName(String baseItemName) {
		this.baseItemName = baseItemName;
	}

	public String getBaseItemCode() {
		return baseItemCode;
	}

	public void setBaseItemCode(String baseItemCode) {
		this.baseItemCode = baseItemCode;
	}

	public Integer getBaseItemType() {
		return baseItemType;
	}

	public void setBaseItemType(Integer baseItemType) {
		this.baseItemType = baseItemType;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getDefaultValueStyle() {
		return defaultValueStyle;
	}

	public void setDefaultValueStyle(Integer defaultValueStyle) {
		this.defaultValueStyle = defaultValueStyle;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getDecimalProcessType() {
		return decimalProcessType;
	}

	public void setDecimalProcessType(Integer decimalProcessType) {
		this.decimalProcessType = decimalProcessType;
	}

	public Integer getCalPrecision() {
		return calPrecision;
	}

	public void setCalPrecision(Integer calPrecision) {
		this.calPrecision = calPrecision;
	}

	public String getBaseItemCondition() {
		return baseItemCondition;
	}

	public void setBaseItemCondition(String baseItemCondition) {
		this.baseItemCondition = baseItemCondition;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getDisplayPriority() {
		return displayPriority;
	}

	public void setDisplayPriority(Integer displayPriority) {
		this.displayPriority = displayPriority;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PrPayrollBaseItemPO{" +
			"id=" + id +
			", baseItemName=" + baseItemName +
			", baseItemCode=" + baseItemCode +
			", baseItemType=" + baseItemType +
			", dataType=" + dataType +
			", defaultValueStyle=" + defaultValueStyle +
			", defaultValue=" + defaultValue +
			", decimalProcessType=" + decimalProcessType +
			", calPrecision=" + calPrecision +
			", baseItemCondition=" + baseItemCondition +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
