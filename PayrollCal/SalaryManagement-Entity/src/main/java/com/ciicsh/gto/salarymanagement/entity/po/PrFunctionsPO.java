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
 * 配置表，函数配置表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_functions")
public class PrFunctionsPO extends Model<PrFunctionsPO> {

	private static final long serialVersionUID = 2572353407094116830L;
	/**
     * 函数ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 函数名称, 显示于计算项公式设计器里
     */
	private String name;
    /**
     * 规则名称
     */
	@TableField("rule_name")
	private String ruleName;
    /**
     * 函数大类
     */
	@TableField("category")
	private String category;
    /**
     * 函数小类
     */
	@TableField("sub_cateogry")
	private String subCateogry;
    /**
     * DRL文件名，一个DRL一个函数
     */
	@TableField("drl_file")
	private String drlFile;
    /**
     * 形参集合
     */
	@TableField("param_set")
	private String paramSet;
    /**
     * 备注说明
     */
	@TableField("remark")
	private String remark;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCateogry() {
		return subCateogry;
	}

	public void setSubCateogry(String subCateogry) {
		this.subCateogry = subCateogry;
	}

	public String getDrlFile() {
		return drlFile;
	}

	public void setDrlFile(String drlFile) {
		this.drlFile = drlFile;
	}

	public String getParamSet() {
		return paramSet;
	}

	public void setParamSet(String paramSet) {
		this.paramSet = paramSet;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PrFunctionsPO{" +
			"id=" + id +
			", name=" + name +
			", ruleName=" + ruleName +
			", category=" + category +
			", subCateogry=" + subCateogry +
			", drlFile=" + drlFile +
			", paramSet=" + paramSet +
			", remark=" + remark +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
