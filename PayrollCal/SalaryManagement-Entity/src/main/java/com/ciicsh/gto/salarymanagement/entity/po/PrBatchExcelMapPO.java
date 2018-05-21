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
 * @author Bill Hu
 * @since 2018-05-05
 */
@TableName("pr_batch_excel_map")
public class PrBatchExcelMapPO extends Model<PrBatchExcelMapPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 函数ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 批次编号
     */
	@TableField("batch_code")
	private String batchCode;
    /**
     * 批次导入：薪资结构与excel列映射关系
     */
	@TableField("mapping_result")
	private String mappingResult;
    /**
     * 唯一性验证映射
     */
	@TableField("identity_result")
	private String identityResult;

	@TableField("excel_cols")
	private String excelCols;

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

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getMappingResult() {
		return mappingResult;
	}

	public void setMappingResult(String mappingResult) {
		this.mappingResult = mappingResult;
	}

	public String getIdentityResult() {
		return identityResult;
	}

	public void setIdentityResult(String identityResult) {
		this.identityResult = identityResult;
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

	public String getExcelCols() {
		return excelCols;
	}

	public void setExcelCols(String excelCols) {
		this.excelCols = excelCols;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PrBatchExcelMapPO{" +
			"id=" + id +
			", batchCode=" + batchCode +
			", excelCols=" + excelCols +
				", mappingResult=" + mappingResult +
			", identityResult=" + identityResult +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
