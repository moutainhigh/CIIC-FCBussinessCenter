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
 * 配置表，公式配置表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_formulas")
public class PrFormulasPO extends Model<PrFormulasPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 公式ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 薪资项ID
     */
	@TableField("item_id")
	private Integer itemId;
    /**
     * 公式内容
     */
	private String content;
    /**
     * 公式说明
     */
	private String desc;
    /**
     * 公式条件
     */
	private String condition;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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
		return "PrFormulasPO{" +
			"id=" + id +
			", itemId=" + itemId +
			", content=" + content +
			", desc=" + desc +
			", condition=" + condition +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
