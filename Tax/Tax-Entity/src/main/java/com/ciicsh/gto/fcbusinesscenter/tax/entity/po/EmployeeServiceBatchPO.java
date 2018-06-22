package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 雇员批次服务信息
 * </p>
 *
 * @author wuhua
 */
@TableName("tax_fc_employee_service_batch")
public class EmployeeServiceBatchPO extends Model<EmployeeServiceBatchPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 计算批次明细ID
     */
	private Long calBatchDetailId;
    /**
     * 申报账户
     */
	private String declareAccount;
    /**
     * 缴纳账户
     */
	private String payAccount;
    /**
     * 供应商收款账户
     */
	private String receiptAccount;
    /**
     * 供应商编号
     */
	private String supportNo;
    /**
     * 供应商名称
     */
	private String supportName;
    /**
     * 是否供应商处理
     */
	private Boolean isSupport;
    /**
     * 是否供应商完成处理
     */
	private Boolean isSupported;
    /**
     * 是否有缴纳服务
     */
	private Boolean isPay;
    /**
     * 是否完成缴纳
     */
	private Boolean isPayed;
    /**
     * 是否有申报服务
     */
	private Boolean isDeclare;
    /**
     * 是否完成申报
     */
	private Boolean isDeclared;
    /**
     * 是否有划款服务
     */
	private Boolean isTransfer;
    /**
     * 是否完成划款
     */
	private Boolean isTransferred;
    /**
     * 是否供应商申报
     */
	private Boolean isDeclareSupported;
    /**
     * 是否供应商划款
     */
	private Boolean isTransferSupported;
    /**
     * 是否供应商缴纳
     */
	private Boolean isPaySupported;
    /**
     * 是否供应商完税凭证
     */
	private Boolean isProofSupported;
    /**
     * 是否有完税凭证服务
     */
	private Boolean isProof;
    /**
     * 是否完成完税凭证
     */
	private Boolean isProofed;
    /**
     * 雇员编号
     */
	private String employeeNo;
    /**
     * 账户类型(00:独立户,01:大库)
     */
	private String accountType;
    /**
     * 区域类型(00:本地,01:异地)
     */
	private String areaType;
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

	public Boolean getProofSupported() {
		return isProofSupported;
	}

	public void setProofSupported(Boolean proofSupported) {
		isProofSupported = proofSupported;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCalBatchDetailId() {
		return calBatchDetailId;
	}

	public void setCalBatchDetailId(Long calBatchDetailId) {
		this.calBatchDetailId = calBatchDetailId;
	}

	public String getDeclareAccount() {
		return declareAccount;
	}

	public void setDeclareAccount(String declareAccount) {
		this.declareAccount = declareAccount;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getReceiptAccount() {
		return receiptAccount;
	}

	public void setReceiptAccount(String receiptAccount) {
		this.receiptAccount = receiptAccount;
	}

	public String getSupportNo() {
		return supportNo;
	}

	public void setSupportNo(String supportNo) {
		this.supportNo = supportNo;
	}

	public String getSupportName() {
		return supportName;
	}

	public void setSupportName(String supportName) {
		this.supportName = supportName;
	}

	public Boolean getSupport() {
		return isSupport;
	}

	public void setSupport(Boolean isSupport) {
		this.isSupport = isSupport;
	}

	public Boolean getSupported() {
		return isSupported;
	}

	public void setSupported(Boolean isSupported) {
		this.isSupported = isSupported;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
	}

	public Boolean getPayed() {
		return isPayed;
	}

	public void setPayed(Boolean isPayed) {
		this.isPayed = isPayed;
	}

	public Boolean getDeclare() {
		return isDeclare;
	}

	public void setDeclare(Boolean isDeclare) {
		this.isDeclare = isDeclare;
	}

	public Boolean getDeclared() {
		return isDeclared;
	}

	public void setDeclared(Boolean isDeclared) {
		this.isDeclared = isDeclared;
	}

	public Boolean getTransfer() {
		return isTransfer;
	}

	public void setTransfer(Boolean isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Boolean getTransferred() {
		return isTransferred;
	}

	public void setTransferred(Boolean isTransferred) {
		this.isTransferred = isTransferred;
	}

	public Boolean getDeclareSupported() {
		return isDeclareSupported;
	}

	public void setDeclareSupported(Boolean isDeclareSupported) {
		this.isDeclareSupported = isDeclareSupported;
	}

	public Boolean getTransferSupported() {
		return isTransferSupported;
	}

	public void setTransferSupported(Boolean isTransferSupported) {
		this.isTransferSupported = isTransferSupported;
	}

	public Boolean getPaySupported() {
		return isPaySupported;
	}

	public void setPaySupported(Boolean isPaySupported) {
		this.isPaySupported = isPaySupported;
	}

	public Boolean getProof() {
		return isProof;
	}

	public void setProof(Boolean isProof) {
		this.isProof = isProof;
	}

	public Boolean getProofed() {
		return isProofed;
	}

	public void setProofed(Boolean isProofed) {
		this.isProofed = isProofed;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "EmployeeServiceBatch{" +
			"id=" + id +
			", calBatchDetailId=" + calBatchDetailId +
			", declareAccount=" + declareAccount +
			", payAccount=" + payAccount +
			", receiptAccount=" + receiptAccount +
			", supportNo=" + supportNo +
			", supportName=" + supportName +
			", isSupport=" + isSupport +
			", isSupported=" + isSupported +
			", isPay=" + isPay +
			", isPayed=" + isPayed +
			", isDeclare=" + isDeclare +
			", isDeclared=" + isDeclared +
			", isTransfer=" + isTransfer +
			", isTransferred=" + isTransferred +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", isDeclareSupported=" + isDeclareSupported +
			", isTransferSupported=" + isTransferSupported +
			", isPaySupported=" + isPaySupported +
			", isProof=" + isProof +
			", isProofed=" + isProofed +
			", employeeNo=" + employeeNo +
			", accountType=" + accountType +
			", areaType=" + areaType +
			"}";
	}
}
