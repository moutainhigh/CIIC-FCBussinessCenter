package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 计算批次
 * </p>
 *
· * @author wuhua
 * @since 2017-12-26
 */
@TableName("tax_fc_calculation_batch")
public class CalculationBatchPO extends Model<CalculationBatchPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 引擎计算批次号
     */
	private String batchNo;
    /**
     * 管理方编号
     */
	private String managerNo;
    /**
     * 管理方名称
     */
	private String managerName;
    /**
     * 个税总金额
     */
	private BigDecimal taxAmount;
    /**
     * 总人数
     */
	private Integer headcount;
    /**
     * 中方人数
     */
	private Integer chineseNum;
    /**
     * 外方人数
     */
	private Integer foreignerNum;
    /**
     * 状态（未提交、审批中、审批通过、已关账）
     */
	private String status;

	/**
	 * 状态中文
	 */
	@TableField(exist = false)

	private String statusName;
    /**
     * 是否可用
     */
    @TableLogic
	private Boolean isActive;
    /**
     * 创建时间
     */
    @TableField(value="created_time",fill = FieldFill.INSERT)
	private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    @TableField(value="modified_time",fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime modifiedTime;
    /**
     * 创建人
     */
    @TableField(value="created_by",fill = FieldFill.INSERT)
	private String createdBy;
    /**
     * 修改人
     */
    @TableField(value="modified_by",fill = FieldFill.INSERT_UPDATE)
	private String modifiedBy;

	/**
	 *主任务id
	 */
	private Long taskMainId;

	/**
	 *批次任务关联
	 */
	@TableField(exist = false)
	private List<CalculationBatchTaskMainPO> calculationBatchTaskMainPOs;

	/**
	 *任务编号集
	 */
	@TableField(exist = false)
	private String taskNos;

	private String batchType;

	private String parentBatchNo;

	@TableField(exist = false)
	private String batchTypeName;

	private Integer versionNo;

	/**
	 * 是否有效
	 */
	private Boolean isValid;

	/**
	 * 创建人displayname
	 */
	@TableField(value="created_by_display_name",fill = FieldFill.INSERT)
	private String createdByDisplayName;
	/**
	 * 修改人displayname
	 */
	@TableField(value="modified_by_display_name",fill = FieldFill.INSERT_UPDATE)
	private String modifiedByDisplayName;

	public String getCreatedByDisplayName() {
		return createdByDisplayName;
	}

	public void setCreatedByDisplayName(String createdByDisplayName) {
		this.createdByDisplayName = createdByDisplayName;
	}

	public String getModifiedByDisplayName() {
		return modifiedByDisplayName;
	}

	public void setModifiedByDisplayName(String modifiedByDisplayName) {
		this.modifiedByDisplayName = modifiedByDisplayName;
	}

	public Boolean getValid() {
		return isValid;
	}

	public void setValid(Boolean valid) {
		isValid = valid;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getBatchTypeName() {
		return batchTypeName;
	}

	public void setBatchTypeName(String batchTypeName) {
		this.batchTypeName = batchTypeName;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;

		if(batchType!=null){

			this.batchTypeName  = EnumUtil.getMessage(EnumUtil.BATCH_TYPE,batchType);
		}
	}

	public String getParentBatchNo() {
		return parentBatchNo;
	}

	public void setParentBatchNo(String parentBatchNo) {
		this.parentBatchNo = parentBatchNo;
	}

	public String getTaskNos() {
		return taskNos;
	}

	public void setTaskNos(String taskNos) {
		this.taskNos = taskNos;
	}

	public List<CalculationBatchTaskMainPO> getCalculationBatchTaskMainPOs() {
		return calculationBatchTaskMainPOs;
	}

	public void setCalculationBatchTaskMainPOs(List<CalculationBatchTaskMainPO> calculationBatchTaskMainPOs) {
		this.calculationBatchTaskMainPOs = calculationBatchTaskMainPOs;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getManagerNo() {
		return managerNo;
	}

	public void setManagerNo(String managerNo) {
		this.managerNo = managerNo;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Integer getHeadcount() {
		return headcount;
	}

	public void setHeadcount(Integer headcount) {
		this.headcount = headcount;
	}

	public Integer getChineseNum() {
		return chineseNum;
	}

	public void setChineseNum(Integer chineseNum) {
		this.chineseNum = chineseNum;
	}

	public Integer getForeignerNum() {
		return foreignerNum;
	}

	public void setForeignerNum(Integer foreignerNum) {
		this.foreignerNum = foreignerNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;

		if(status!=null){

			this.statusName  = EnumUtil.getMessage(EnumUtil.BATCH_NO_STATUS,status);
		}

	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/*public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}*/

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Long getTaskMainId() {
		return taskMainId;
	}

	public void setTaskMainId(Long taskMainId) {
		this.taskMainId = taskMainId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CalculationBatch{" +
			"id=" + id +
			", batchNo=" + batchNo +
			", managerNo=" + managerNo +
			", managerName=" + managerName +
			", taxAmount=" + taxAmount +
			", headcount=" + headcount +
			", chineseNum=" + chineseNum +
			", foreignerNum=" + foreignerNum +
			", status=" + status +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
