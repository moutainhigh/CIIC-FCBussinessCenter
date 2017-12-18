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
 * 业务编码自增序列
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@TableName("pr_code_sequence")
public class PrCodeSequencePO extends Model<PrCodeSequencePO> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 序列名
     */
	@TableField("seq_name")
	private String seqName;
    /**
     * 前缀
     */
	private String prefix;
    /**
     * 当前序列值
     */
	@TableField("current_val")
	private Long currentVal;
    /**
     * 增幅
     */
	@TableField("increment_val")
	private Long incrementVal;
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

	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Long getCurrentVal() {
		return currentVal;
	}

	public void setCurrentVal(Long currentVal) {
		this.currentVal = currentVal;
	}

	public Long getIncrementVal() {
		return incrementVal;
	}

	public void setIncrementVal(Long incrementVal) {
		this.incrementVal = incrementVal;
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
		return "PrCodeSequencePO{" +
			"id=" + id +
			", seqName=" + seqName +
			", prefix=" + prefix +
			", currentVal=" + currentVal +
			", incrementVal=" + incrementVal +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
