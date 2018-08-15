package com.ciicsh.gto.salarymanagement.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.*;
import java.util.Date;

/**
 * <p>
 * 配置表，薪资项明细表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_payroll_item")
public class PrPayrollItemPO extends Model<PrPayrollItemPO> implements Serializable{

	private static final long serialVersionUID = 9027912774188282467L;
	/**
     * 薪资项Id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 薪资项编码, 规则为XZX-9位管理方ID-薪资项类型缩写-3位数字序号
     */
	@TableField("item_code")
	private String itemCode;
    /**
     * 继承薪资组模板薪资项Code: 非必选项，让用户可以不需模板直接产生薪资项。
     */
	@TableField("parent_item_code")
	private String parentItemCode;
    /**
     * 所属管理方ID
     */
	@TableField("management_id")
	private String managementId;
	/**
	 * 薪资组编码
	 */
	@TableField("payroll_group_code")
	private String payrollGroupCode;
	/**
	 * 薪资组模版编码
	 */
	@TableField("payroll_group_template_code")
	private String payrollGroupTemplateCode;
    /**
     * 薪资项名称
     */
	@TableField("item_name")
	private String itemName;
    /**
     * 薪资项类型：
1 - 固定输入项，一次初始化，很少变更，建立批次时一般直接使用 
2 - 可变输入项，每建立一个批次，需要从FC客户中心服务接口导入值
3-计算项，通过配置好的公式计算得出的薪资项，如：加班工资，病假工资等
     */
	@TableField("item_type")
	private Integer itemType;
    /**
     * 基类薪资项Code
     */
	@TableField("base_item_code")
	private String baseItemCode;
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
     * 公式原始内容
     */
	@TableField("origin_formula")
	private String originFormula;
    /**
     * 公式内容
     */
	@TableField("formula_content")
	private String formulaContent;
    /**
     * 薪资项取值范围的条件描述
     */
	@TableField("item_condition")
	private String itemCondition;
    /**
     * 计算顺序
     */
	@TableField("cal_priority")
	private Integer calPriority;
    /**
     * 显示顺序
     */
	@TableField("display_priority")
	private Integer displayPriority;
    /**
     * 备注
     */
	private String remark;
	/**
	 * 是否可以上锁
	 */
	@TableField("can_lock")
	private Boolean canLock;
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
	 * 薪资项值用于计算扩展
	 */
	@TableField("item_value")
	private String itemValue;

    /**
     * 条件原始输入值
     */
	@TableField("origin_condition")
	private String originCondition;

	/**
	 * 完整公式结构体
	 */
	@TableField("full_formula")
	private String fullFormula;

	// 操作类型 1:添加; 2:更新; 3:删除; 4:查询; 5:导入; 6:复制;
	@TableField(exist = false)
	private int operateType;

    /**
     * 扩展标识：1-扩展字段；0-非扩展字段
     */
    @TableField("extend_flag")
    private Integer extendFlag;

    public Integer getId() {
        return id;
    }

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getParentItemCode() {
		return parentItemCode;
	}

	public void setParentItemCode(String parentItemCode) {
		this.parentItemCode = parentItemCode;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
	}

	public String getPayrollGroupCode() {
		return payrollGroupCode;
	}

	public void setPayrollGroupCode(String payrollGroupCode) {
		this.payrollGroupCode = payrollGroupCode;
	}

	public String getPayrollGroupTemplateCode() {
		return payrollGroupTemplateCode;
	}

	public void setPayrollGroupTemplateCode(String payrollGroupTemplateCode) {
		this.payrollGroupTemplateCode = payrollGroupTemplateCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public String getBaseItemCode() {
		return baseItemCode;
	}

	public void setBaseItemCode(String baseItemCode) {
		this.baseItemCode = baseItemCode;
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

    public String getOriginFormula() {
        return originFormula;
    }

    public void setOriginFormula(String originFormula) {
        this.originFormula = originFormula;
    }

    public String getFormulaContent() {
		return formulaContent;
	}

	public void setFormulaContent(String formulaContent) {
		this.formulaContent = formulaContent;
	}

	public String getItemCondition() {
		return itemCondition;
	}

	public void setItemCondition(String itemCondition) {
		this.itemCondition = itemCondition;
	}

	public Integer getCalPriority() {
		return calPriority;
	}

	public void setCalPriority(Integer calPriority) {
		this.calPriority = calPriority;
	}

	public Integer getDisplayPriority() {
		return displayPriority;
	}

	public void setDisplayPriority(Integer displayPriority) {
		this.displayPriority = displayPriority;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

    public String getOriginCondition() {
        return originCondition;
    }

    public void setOriginCondition(String originCondition) {
        this.originCondition = originCondition;
    }

	public String getFullFormula() {
		return fullFormula;
	}

	public void setFullFormula(String fullFormula) {
		this.fullFormula = fullFormula;
	}

	public Boolean getCanLock() {
		return canLock;
	}

	public void setCanLock(Boolean canLock) {
		this.canLock = canLock;
	}

    public Integer getExtendFlag() {
        return extendFlag;
    }

    public void setExtendFlag(Integer extendFlag) {
        this.extendFlag = extendFlag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}


	@Override
	public String toString() {
		return "PrPayrollItemPO{" +
			"id=" + id +
			", itemCode=" + itemCode +
			", parentItemCode=" + parentItemCode +
			", managementId=" + managementId +
			", payrollGroupCode=" + payrollGroupCode +
			", payrollGroupTemplateCode=" + payrollGroupTemplateCode +
			", itemName=" + itemName +
			", itemType=" + itemType +
			", baseItemCode=" + baseItemCode +
			", dataType=" + dataType +
			", defaultValueStyle=" + defaultValueStyle +
			", defaultValue=" + defaultValue +
			", decimalProcessType=" + decimalProcessType +
			", calPrecision=" + calPrecision +
			", formulaContent=" + formulaContent +
			", itemCondition=" + itemCondition +
			", calPriority=" + calPriority +
			", displayPriority=" + displayPriority +
			", remark=" + remark +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
            ", itemValue=" + itemValue +
            ", originCondition=" + originCondition +
            ", fullFormula=" + fullFormula +
            ", extendFlag=" + extendFlag +
			"}";
	}

	public PrPayrollItemPO deepClone() {
		PrPayrollItemPO outer = null;
	    try {
	    	// 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			// 将流序列化成对象
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			outer = (PrPayrollItemPO) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return outer;
  	}
}
