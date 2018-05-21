package com.ciicsh.gto.fcbusinesscenter.tax.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 雇员个税信息
 * </p>
 *
 * @author wuhua
 * @since 2018-05-21
 */
@TableName("tax_fc_employee_info_batch")
public class EmployeeInfoBatchPO extends Model<EmployeeInfoBatchPO> {

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
     * 国籍
     */
	private String nationality;
    /**
     * 是否残疾烈属孤老
     */
	private Boolean isDisability;
    /**
     * 是否雇员
     */
	private Boolean isEmployee;
    /**
     * 是否股东投资者
     */
	private Boolean isInvestor;
    /**
     * 是否境外人员
     */
	private Boolean isOverseas;
    /**
     * 个人股本（投资）额
     */
	private BigDecimal personalInvestment;
    /**
     * 外籍员工中文姓名
     */
	private String chineseName;
    /**
     * 来华时间
     */
	private Date comingToChinaDate;
    /**
     * 任职期限
     */
	private Date termOfService;
    /**
     * 预计离境时间
     */
	private Date expectedLeaveDate;
    /**
     * 预计离境地点
     */
	private Date expectedLeavePlace;
    /**
     * 境内职务
     */
	private String domesticDuty;
    /**
     * 境外职务
     */
	private String overseasDuty;
    /**
     * 支付地
     */
	private String paymentPlace;
    /**
     * 境外支付地
     */
	private String paymentOverseasPlace;
    /**
     * 税款负担方式
     */
	private String burden;
    /**
     * 税优识别码
     */
	private String recognitionCode;
    /**
     * 年度保费
     */
	private BigDecimal annualPremium;
    /**
     * 月度保费
     */
	private BigDecimal monthlyPremium;
    /**
     * 股票期权收入类型
     */
	private String stockOptionCategory;
    /**
     * 股票名称(简称)
     */
	private String stockName;
    /**
     * 股票代码
     */
	private String stockCode;
    /**
     * 股票类型
     */
	private String stockType;
    /**
     * (期权性)行权股票的每股市场价
     */
	private BigDecimal optionMarketValue;
    /**
     * (期权性)股票期权支付的每股施权价
     */
	private BigDecimal optionExerciseValue;
    /**
     * (期权性)股票数量
     */
	private Integer optionQuantity;
    /**
     * 本月行权收入
     */
	private BigDecimal optionExerciseIncome;
    /**
     * (增值性)行权日股票价格
     */
	private BigDecimal incrementExerciseValue;
    /**
     * (增值性)施权日股票价格
     */
	private BigDecimal incrementImplementValue;
    /**
     * (增值性)行权股票份数
     */
	private Integer incrementQuantity;
    /**
     * 本月行权收入
     */
	private BigDecimal incrementExerciseIncome;
    /**
     * (限制性)股票登记日股票市价
     */
	private BigDecimal restrictRegisterValue;
    /**
     * (限制性)本批次解禁股票当日市价
     */
	private BigDecimal restrictRelieveValue;
    /**
     * (限制性)本批次解禁股票份数
     */
	private Integer restrictRelieveQuantity;
    /**
     * (限制性)被激励对象获得的限制性股票总份数
     */
	private Integer restrictTotal;
    /**
     * (限制性)被激励对象实际支付的资金总额
     */
	private BigDecimal restrictPayment;
    /**
     * 本月行权收入
     */
	private BigDecimal restrictExerciseIncome;
    /**
     * 是否可用
     */
    @TableLogic
	private Boolean isActive;
    /**
     * 创建时间
     */
	private Date createdTime;
    /**
     * 修改时间
     */
	private Date modifiedTime;
    /**
     * 创建人
     */
	private String createdBy;
    /**
     * 修改人
     */
	private String modifiedBy;


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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Boolean getDisability() {
		return isDisability;
	}

	public void setDisability(Boolean isDisability) {
		this.isDisability = isDisability;
	}

	public Boolean getEmployee() {
		return isEmployee;
	}

	public void setEmployee(Boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public Boolean getInvestor() {
		return isInvestor;
	}

	public void setInvestor(Boolean isInvestor) {
		this.isInvestor = isInvestor;
	}

	public Boolean getOverseas() {
		return isOverseas;
	}

	public void setOverseas(Boolean isOverseas) {
		this.isOverseas = isOverseas;
	}

	public BigDecimal getPersonalInvestment() {
		return personalInvestment;
	}

	public void setPersonalInvestment(BigDecimal personalInvestment) {
		this.personalInvestment = personalInvestment;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public Date getComingToChinaDate() {
		return comingToChinaDate;
	}

	public void setComingToChinaDate(Date comingToChinaDate) {
		this.comingToChinaDate = comingToChinaDate;
	}

	public Date getTermOfService() {
		return termOfService;
	}

	public void setTermOfService(Date termOfService) {
		this.termOfService = termOfService;
	}

	public Date getExpectedLeaveDate() {
		return expectedLeaveDate;
	}

	public void setExpectedLeaveDate(Date expectedLeaveDate) {
		this.expectedLeaveDate = expectedLeaveDate;
	}

	public Date getExpectedLeavePlace() {
		return expectedLeavePlace;
	}

	public void setExpectedLeavePlace(Date expectedLeavePlace) {
		this.expectedLeavePlace = expectedLeavePlace;
	}

	public String getDomesticDuty() {
		return domesticDuty;
	}

	public void setDomesticDuty(String domesticDuty) {
		this.domesticDuty = domesticDuty;
	}

	public String getOverseasDuty() {
		return overseasDuty;
	}

	public void setOverseasDuty(String overseasDuty) {
		this.overseasDuty = overseasDuty;
	}

	public String getPaymentPlace() {
		return paymentPlace;
	}

	public void setPaymentPlace(String paymentPlace) {
		this.paymentPlace = paymentPlace;
	}

	public String getPaymentOverseasPlace() {
		return paymentOverseasPlace;
	}

	public void setPaymentOverseasPlace(String paymentOverseasPlace) {
		this.paymentOverseasPlace = paymentOverseasPlace;
	}

	public String getBurden() {
		return burden;
	}

	public void setBurden(String burden) {
		this.burden = burden;
	}

	public String getRecognitionCode() {
		return recognitionCode;
	}

	public void setRecognitionCode(String recognitionCode) {
		this.recognitionCode = recognitionCode;
	}

	public BigDecimal getAnnualPremium() {
		return annualPremium;
	}

	public void setAnnualPremium(BigDecimal annualPremium) {
		this.annualPremium = annualPremium;
	}

	public BigDecimal getMonthlyPremium() {
		return monthlyPremium;
	}

	public void setMonthlyPremium(BigDecimal monthlyPremium) {
		this.monthlyPremium = monthlyPremium;
	}

	public String getStockOptionCategory() {
		return stockOptionCategory;
	}

	public void setStockOptionCategory(String stockOptionCategory) {
		this.stockOptionCategory = stockOptionCategory;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public BigDecimal getOptionMarketValue() {
		return optionMarketValue;
	}

	public void setOptionMarketValue(BigDecimal optionMarketValue) {
		this.optionMarketValue = optionMarketValue;
	}

	public BigDecimal getOptionExerciseValue() {
		return optionExerciseValue;
	}

	public void setOptionExerciseValue(BigDecimal optionExerciseValue) {
		this.optionExerciseValue = optionExerciseValue;
	}

	public Integer getOptionQuantity() {
		return optionQuantity;
	}

	public void setOptionQuantity(Integer optionQuantity) {
		this.optionQuantity = optionQuantity;
	}

	public BigDecimal getOptionExerciseIncome() {
		return optionExerciseIncome;
	}

	public void setOptionExerciseIncome(BigDecimal optionExerciseIncome) {
		this.optionExerciseIncome = optionExerciseIncome;
	}

	public BigDecimal getIncrementExerciseValue() {
		return incrementExerciseValue;
	}

	public void setIncrementExerciseValue(BigDecimal incrementExerciseValue) {
		this.incrementExerciseValue = incrementExerciseValue;
	}

	public BigDecimal getIncrementImplementValue() {
		return incrementImplementValue;
	}

	public void setIncrementImplementValue(BigDecimal incrementImplementValue) {
		this.incrementImplementValue = incrementImplementValue;
	}

	public Integer getIncrementQuantity() {
		return incrementQuantity;
	}

	public void setIncrementQuantity(Integer incrementQuantity) {
		this.incrementQuantity = incrementQuantity;
	}

	public BigDecimal getIncrementExerciseIncome() {
		return incrementExerciseIncome;
	}

	public void setIncrementExerciseIncome(BigDecimal incrementExerciseIncome) {
		this.incrementExerciseIncome = incrementExerciseIncome;
	}

	public BigDecimal getRestrictRegisterValue() {
		return restrictRegisterValue;
	}

	public void setRestrictRegisterValue(BigDecimal restrictRegisterValue) {
		this.restrictRegisterValue = restrictRegisterValue;
	}

	public BigDecimal getRestrictRelieveValue() {
		return restrictRelieveValue;
	}

	public void setRestrictRelieveValue(BigDecimal restrictRelieveValue) {
		this.restrictRelieveValue = restrictRelieveValue;
	}

	public Integer getRestrictRelieveQuantity() {
		return restrictRelieveQuantity;
	}

	public void setRestrictRelieveQuantity(Integer restrictRelieveQuantity) {
		this.restrictRelieveQuantity = restrictRelieveQuantity;
	}

	public Integer getRestrictTotal() {
		return restrictTotal;
	}

	public void setRestrictTotal(Integer restrictTotal) {
		this.restrictTotal = restrictTotal;
	}

	public BigDecimal getRestrictPayment() {
		return restrictPayment;
	}

	public void setRestrictPayment(BigDecimal restrictPayment) {
		this.restrictPayment = restrictPayment;
	}

	public BigDecimal getRestrictExerciseIncome() {
		return restrictExerciseIncome;
	}

	public void setRestrictExerciseIncome(BigDecimal restrictExerciseIncome) {
		this.restrictExerciseIncome = restrictExerciseIncome;
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
		return "EmployeeInfoBatchPO{" +
			"id=" + id +
			", calBatchDetailId=" + calBatchDetailId +
			", nationality=" + nationality +
			", isDisability=" + isDisability +
			", isEmployee=" + isEmployee +
			", isInvestor=" + isInvestor +
			", isOverseas=" + isOverseas +
			", personalInvestment=" + personalInvestment +
			", chineseName=" + chineseName +
			", comingToChinaDate=" + comingToChinaDate +
			", termOfService=" + termOfService +
			", expectedLeaveDate=" + expectedLeaveDate +
			", expectedLeavePlace=" + expectedLeavePlace +
			", domesticDuty=" + domesticDuty +
			", overseasDuty=" + overseasDuty +
			", paymentPlace=" + paymentPlace +
			", paymentOverseasPlace=" + paymentOverseasPlace +
			", burden=" + burden +
			", recognitionCode=" + recognitionCode +
			", annualPremium=" + annualPremium +
			", monthlyPremium=" + monthlyPremium +
			", stockOptionCategory=" + stockOptionCategory +
			", stockName=" + stockName +
			", stockCode=" + stockCode +
			", stockType=" + stockType +
			", optionMarketValue=" + optionMarketValue +
			", optionExerciseValue=" + optionExerciseValue +
			", optionQuantity=" + optionQuantity +
			", optionExerciseIncome=" + optionExerciseIncome +
			", incrementExerciseValue=" + incrementExerciseValue +
			", incrementImplementValue=" + incrementImplementValue +
			", incrementQuantity=" + incrementQuantity +
			", incrementExerciseIncome=" + incrementExerciseIncome +
			", restrictRegisterValue=" + restrictRegisterValue +
			", restrictRelieveValue=" + restrictRelieveValue +
			", restrictRelieveQuantity=" + restrictRelieveQuantity +
			", restrictTotal=" + restrictTotal +
			", restrictPayment=" + restrictPayment +
			", restrictExerciseIncome=" + restrictExerciseIncome +
			", isActive=" + isActive +
			", createdTime=" + createdTime +
			", modifiedTime=" + modifiedTime +
			", createdBy=" + createdBy +
			", modifiedBy=" + modifiedBy +
			"}";
	}
}
