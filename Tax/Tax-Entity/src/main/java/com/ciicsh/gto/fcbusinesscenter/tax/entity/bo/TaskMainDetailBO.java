package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author wuhua
 */
public class TaskMainDetailBO {

    private Long id;

    private Long calculationBatchDetailId;

    private Long taskMainId;
    private Long taskMainDetailId;

    private String employeeNo;

    private String employeeName;

    private String idType;

    private String idTypeName;

    private String idNo;

    /**
     * 管理方名称
     */
    private String managerName;

    /**
     * 薪酬计算批次
     */
    private String batchNo;

    /**
     * 所得期间
     */
    private LocalDate period;
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
     * 是否有完税凭证服务
     */
    private Boolean isProof;
    /**
     * 是否完税凭证服务完成
     */
    private Boolean isProofed;

    /**
     * 是否合并已确认
     */
    private Boolean isCombineConfirmed;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * 区域类型
     */
    private String areaType;

    private String managerNo;

    private BigDecimal taxReal;

    //税前合计
    private BigDecimal preTaxAggregate;

    //免税津贴
    private BigDecimal dutyFreeAllowance;

    //商业健康保险费
    private BigDecimal businessHealthInsurance;

    //年金
    private BigDecimal annuity;

    public BigDecimal getPreTaxAggregate() {
        return preTaxAggregate;
    }

    public void setPreTaxAggregate(BigDecimal preTaxAggregate) {
        this.preTaxAggregate = preTaxAggregate;
    }

    public BigDecimal getDutyFreeAllowance() {
        return dutyFreeAllowance;
    }

    public void setDutyFreeAllowance(BigDecimal dutyFreeAllowance) {
        this.dutyFreeAllowance = dutyFreeAllowance;
    }

    public BigDecimal getBusinessHealthInsurance() {
        return businessHealthInsurance;
    }

    public void setBusinessHealthInsurance(BigDecimal businessHealthInsurance) {
        this.businessHealthInsurance = businessHealthInsurance;
    }

    public BigDecimal getAnnuity() {
        return annuity;
    }

    public void setAnnuity(BigDecimal annuity) {
        this.annuity = annuity;
    }

    public BigDecimal getTaxReal() {
        return taxReal;
    }

    public void setTaxReal(BigDecimal taxReal) {
        this.taxReal = taxReal;
    }

    public Long getCalculationBatchDetailId() {
        return calculationBatchDetailId;
    }

    public void setCalculationBatchDetailId(Long calculationBatchDetailId) {
        this.calculationBatchDetailId = calculationBatchDetailId;
    }

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public Boolean getProof() {
        return isProof;
    }

    public void setProof(Boolean proof) {
        isProof = proof;
    }

    public Boolean getProofed() {
        return isProofed;
    }

    public void setProofed(Boolean proofed) {
        isProofed = proofed;
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

    public Boolean getCombineConfirmed() {
        return isCombineConfirmed;
    }

    public void setCombineConfirmed(Boolean combineConfirmed) {
        isCombineConfirmed = combineConfirmed;
    }

    public Long getTaskMainId() {
        return taskMainId;
    }

    public void setTaskMainId(Long taskMainId) {
        this.taskMainId = taskMainId;
    }

    public Long getTaskMainDetailId() {
        return taskMainDetailId;
    }

    public void setTaskMainDetailId(Long taskMainDetailId) {
        this.taskMainDetailId = taskMainDetailId;
    }

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

        if(idType!=null){

            this.idTypeName  = EnumUtil.getMessage(EnumUtil.IT_TYPE,idType);
        }
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public String getIncomeSubject() {
        return incomeSubject;
    }

    public void setIncomeSubject(String incomeSubject) {
        this.incomeSubject = incomeSubject;

        if(incomeSubject!=null){

            this.incomeSubjectName  = EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,incomeSubject);
        }
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

    public Boolean getDeclareSupported() {
        return isDeclareSupported;
    }

    public void setDeclareSupported(Boolean declareSupported) {
        isDeclareSupported = declareSupported;
    }

    public Boolean getTransferSupported() {
        return isTransferSupported;
    }

    public void setTransferSupported(Boolean transferSupported) {
        isTransferSupported = transferSupported;
    }

    public Boolean getPaySupported() {
        return isPaySupported;
    }

    public void setPaySupported(Boolean paySupported) {
        isPaySupported = paySupported;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @Override
    public String toString() {
        return "CalculationBatchDetailBO{" +
                "id=" + id +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", idType='" + idType + '\'' +
                ", idTypeName='" + idTypeName + '\'' +
                ", idNo='" + idNo + '\'' +
                ", managerName='" + managerName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", period=" + period +
                ", incomeSubject='" + incomeSubject + '\'' +
                ", incomeSubjectName='" + incomeSubjectName + '\'' +
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
                ", declareAccount='" + declareAccount + '\'' +
                ", payAccount='" + payAccount + '\'' +
                ", receiptAccount='" + receiptAccount + '\'' +
                ", supportNo='" + supportNo + '\'' +
                ", supportName='" + supportName + '\'' +
                ", isSupport=" + isSupport +
                ", isSupported=" + isSupported +
                ", isPay=" + isPay +
                ", isPayed=" + isPayed +
                ", isDeclare=" + isDeclare +
                ", isDeclared=" + isDeclared +
                ", isTranfer=" + isTranfer +
                ", isTranferred=" + isTranferred +
                ", isDeclareSupported=" + isDeclareSupported +
                ", isTransferSupported=" + isTransferSupported +
                ", isPaySupported=" + isPaySupported +
                '}';
    }
}
