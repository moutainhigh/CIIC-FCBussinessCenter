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
 * 批次账户表
 * </p>
 *
 * @author wuhua
 * @since 2018-07-04
 */
@TableName("tax_fc_calculation_batch_account")
public class CalculationBatchAccountPO extends Model<CalculationBatchAccountPO> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 账户名称
	 */
	private String accountName;
	/**
	 * 纳税人识别号
	 */
	private String accountNumber;
	/**
	 * 委托合同流水号
	 */
	private String commissionContractSerialNumber;
	/**
	 * 省份代码
	 */
	private String provinceCode;
	/**
	 * 城市代码
	 */
	private String cityCode;
	/**
	 * 纳税分局
	 */
	private String substation;
	/**
	 * 纳税人开户名
	 */
	private String taxpayerAccountName;
	/**
	 * 纳税人专户帐号
	 */
	private String account;
	/**
	 * 纳税专户开户银行
	 */
	private String taxAccountOpeningBank;
	/**
	 * 纳税人名称
	 */
	private String taxpayerName;
	/**
	 * 所属税务局
	 */
	private String station;
	/**
	 * 来源 0：大库 1：独立库
	 */
	private String source;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCommissionContractSerialNumber() {
		return commissionContractSerialNumber;
	}

	public void setCommissionContractSerialNumber(String commissionContractSerialNumber) {
		this.commissionContractSerialNumber = commissionContractSerialNumber;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getSubstation() {
		return substation;
	}

	public void setSubstation(String substation) {
		this.substation = substation;
	}

	public String getTaxpayerAccountName() {
		return taxpayerAccountName;
	}

	public void setTaxpayerAccountName(String taxpayerAccountName) {
		this.taxpayerAccountName = taxpayerAccountName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTaxAccountOpeningBank() {
		return taxAccountOpeningBank;
	}

	public void setTaxAccountOpeningBank(String taxAccountOpeningBank) {
		this.taxAccountOpeningBank = taxAccountOpeningBank;
	}

	public String getTaxpayerName() {
		return taxpayerName;
	}

	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CalculationBatchAccount{" +
				"id=" + id +
				", accountName=" + accountName +
				", accountNumber=" + accountNumber +
				", commissionContractSerialNumber=" + commissionContractSerialNumber +
				", provinceCode=" + provinceCode +
				", cityCode=" + cityCode +
				", substation=" + substation +
				", taxpayerAccountName=" + taxpayerAccountName +
				", account=" + account +
				", taxAccountOpeningBank=" + taxAccountOpeningBank +
				", taxpayerName=" + taxpayerName +
				", station=" + station +
				", source=" + source +
				", isActive=" + isActive +
				", createdTime=" + createdTime +
				", modifiedTime=" + modifiedTime +
				", createdBy=" + createdBy +
				", modifiedBy=" + modifiedBy +
				", createdByDisplayName=" + createdByDisplayName +
				", modifiedByDisplayName=" + modifiedByDisplayName +
				"}";
	}
}
