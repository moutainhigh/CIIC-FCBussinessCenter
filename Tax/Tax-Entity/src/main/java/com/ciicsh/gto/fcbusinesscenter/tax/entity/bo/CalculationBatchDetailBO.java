package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import com.baomidou.mybatisplus.plugins.Page;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuantongqing on 2017/12/19
 */
public class CalculationBatchDetailBO {

    private Long id;

    private Long calculationBatchId;

    private String employeeNo;

    private String employeeName;

    private String idType;

    private String idTypeName;

    private String idNo;

    /**
     * 所得期间
     */
    private Date period;
    /**
     * 所得项目
     */
    private String incomeSubject;
    private String incomeSubjectName;
    /**
     * 收入额
     */
    private BigDecimal incomeTotal;
    /**
     * 免税所得
     */
    private BigDecimal incomeDutyfree;
    /**
     * 基本养老保险费（税前扣除项目）
     */
    private BigDecimal deductRetirementInsurance;
    /**
     * 基本医疗保险费（税前扣除项目）
     */
    private BigDecimal deductMedicalInsurance;
    /**
     * 失业保险费（税前扣除项目）
     */
    private BigDecimal deductDlenessInsurance;
    /**
     * 财产原值（税前扣除项目）
     */
    private BigDecimal deductProperty;

    /**
     * 住房公积金（税前扣除项目）
     */
    private BigDecimal deductHouseFund;
    /**
     * 允许扣除的税费（税前扣除项目）
     */
    private BigDecimal deductTakeoff;
    /**
     * 其他（税前扣除项目）
     */
    private BigDecimal deductOther;
    /**
     * 合计（税前扣除项目）
     */
    private BigDecimal deductTotal;

    /**
     * 减除费用
     */
    private BigDecimal deduction;
    /**
     * 准予扣除的捐赠额
     */
    private BigDecimal donation;
    /**
     * 应纳税所得额
     */
    private BigDecimal incomeForTax;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 速算扣除数
     */
    private Integer quickCalDeduct;
    /**
     * 应纳税额
     */
    private BigDecimal taxAmount;

    /**
     * 减免税额
     */
    private BigDecimal taxDeduction;
    /**
     * 应扣缴税额
     */
    private BigDecimal taxWithholdAmount;
    /**
     * 已扣缴税额
     */
    private BigDecimal taxWithholdedAmount;
    /**
     * 应补（退）税额
     */
    private BigDecimal taxRemedyOrReturn;

    /**
     * 分页page对象
     */
    private Page page;

    /**
     * 申报账户
     */
    private String declareAccount;

    /**
     * 缴纳账户
     */
    private String payAccount;

    /**
     * 收款账户
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
     * 是否供应商处理完成
     */
    private Boolean isSupported;

    /**
     * 是否有缴纳服务
     */
    private Boolean isPay;

    /**
     * 是否缴纳完成
     */
    private Boolean isPayed;

    /**
     * 是否有申报服务
     */
    private Boolean isDeclare;

    /**
     * 是否申报完成
     */
    private Boolean isDeclared;

    /**
     * 是否有划款服务
     */
    private Boolean isTranfer;

    /**
     * 是否划款完成
     */
    private Boolean isTranferred;

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getIncomeSubjectName() {
        return incomeSubjectName;
    }

    public void setIncomeSubjectName(String incomeSubjectName) {
        this.incomeSubjectName = incomeSubjectName;
    }

    public Long getCalculationBatchId() {
        return calculationBatchId;
    }

    public void setCalculationBatchId(Long calculationBatchId) {
        this.calculationBatchId = calculationBatchId;
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

    public void setSupport(Boolean support) {
        isSupport = support;
    }

    public Boolean getSupported() {
        return isSupported;
    }

    public void setSupported(Boolean supported) {
        isSupported = supported;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }

    public Boolean getDeclare() {
        return isDeclare;
    }

    public void setDeclare(Boolean declare) {
        isDeclare = declare;
    }

    public Boolean getDeclared() {
        return isDeclared;
    }

    public void setDeclared(Boolean declared) {
        isDeclared = declared;
    }

    public Boolean getTranfer() {
        return isTranfer;
    }

    public void setTranfer(Boolean tranfer) {
        isTranfer = tranfer;
    }

    public Boolean getTranferred() {
        return isTranferred;
    }

    public void setTranferred(Boolean tranferred) {
        isTranferred = tranferred;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public String getIncomeSubject() {
        return incomeSubject;
    }

    public void setIncomeSubject(String incomeSubject) {
        this.incomeSubject = incomeSubject;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public BigDecimal getIncomeDutyfree() {
        return incomeDutyfree;
    }

    public void setIncomeDutyfree(BigDecimal incomeDutyfree) {
        this.incomeDutyfree = incomeDutyfree;
    }

    public BigDecimal getDeductRetirementInsurance() {
        return deductRetirementInsurance;
    }

    public void setDeductRetirementInsurance(BigDecimal deductRetirementInsurance) {
        this.deductRetirementInsurance = deductRetirementInsurance;
    }

    public BigDecimal getDeductMedicalInsurance() {
        return deductMedicalInsurance;
    }

    public void setDeductMedicalInsurance(BigDecimal deductMedicalInsurance) {
        this.deductMedicalInsurance = deductMedicalInsurance;
    }

    public BigDecimal getDeductDlenessInsurance() {
        return deductDlenessInsurance;
    }

    public void setDeductDlenessInsurance(BigDecimal deductDlenessInsurance) {
        this.deductDlenessInsurance = deductDlenessInsurance;
    }

    public BigDecimal getDeductProperty() {
        return deductProperty;
    }

    public void setDeductProperty(BigDecimal deductProperty) {
        this.deductProperty = deductProperty;
    }

    public BigDecimal getDeductHouseFund() {
        return deductHouseFund;
    }

    public void setDeductHouseFund(BigDecimal deductHouseFund) {
        this.deductHouseFund = deductHouseFund;
    }

    public BigDecimal getDeductTakeoff() {
        return deductTakeoff;
    }

    public void setDeductTakeoff(BigDecimal deductTakeoff) {
        this.deductTakeoff = deductTakeoff;
    }

    public BigDecimal getDeductOther() {
        return deductOther;
    }

    public void setDeductOther(BigDecimal deductOther) {
        this.deductOther = deductOther;
    }

    public BigDecimal getDeductTotal() {
        return deductTotal;
    }

    public void setDeductTotal(BigDecimal deductTotal) {
        this.deductTotal = deductTotal;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getDonation() {
        return donation;
    }

    public void setDonation(BigDecimal donation) {
        this.donation = donation;
    }

    public BigDecimal getIncomeForTax() {
        return incomeForTax;
    }

    public void setIncomeForTax(BigDecimal incomeForTax) {
        this.incomeForTax = incomeForTax;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getQuickCalDeduct() {
        return quickCalDeduct;
    }

    public void setQuickCalDeduct(Integer quickCalDeduct) {
        this.quickCalDeduct = quickCalDeduct;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(BigDecimal taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public BigDecimal getTaxWithholdAmount() {
        return taxWithholdAmount;
    }

    public void setTaxWithholdAmount(BigDecimal taxWithholdAmount) {
        this.taxWithholdAmount = taxWithholdAmount;
    }

    public BigDecimal getTaxWithholdedAmount() {
        return taxWithholdedAmount;
    }

    public void setTaxWithholdedAmount(BigDecimal taxWithholdedAmount) {
        this.taxWithholdedAmount = taxWithholdedAmount;
    }

    public BigDecimal getTaxRemedyOrReturn() {
        return taxRemedyOrReturn;
    }

    public void setTaxRemedyOrReturn(BigDecimal taxRemedyOrReturn) {
        this.taxRemedyOrReturn = taxRemedyOrReturn;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "CalculationBatchDetailBO{" +
                "id=" + id +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", period=" + period +
                ", incomeSubject='" + incomeSubject + '\'' +
                ", incomeTotal=" + incomeTotal +
                ", incomeDutyfree=" + incomeDutyfree +
                ", deductRetirementInsurance=" + deductRetirementInsurance +
                ", deductMedicalInsurance=" + deductMedicalInsurance +
                ", deductDlenessInsurance=" + deductDlenessInsurance +
                ", deductProperty=" + deductProperty +
                ", deductHouseFund=" + deductHouseFund +
                ", deductTakeoff=" + deductTakeoff +
                ", deductOther=" + deductOther +
                ", deductTotal=" + deductTotal +
                ", deduction=" + deduction +
                ", donation=" + donation +
                ", incomeForTax=" + incomeForTax +
                ", taxRate='" + taxRate + '\'' +
                ", quickCalDeduct=" + quickCalDeduct +
                ", taxAmount=" + taxAmount +
                ", taxDeduction=" + taxDeduction +
                ", taxWithholdAmount=" + taxWithholdAmount +
                ", taxWithholdedAmount=" + taxWithholdedAmount +
                ", taxRemedyOrReturn=" + taxRemedyOrReturn +
                ", page=" + page +
                '}';
    }
}
