package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.frombatch;

import java.math.BigDecimal;

public class CalResultBO {
    /**
     * 薪金个税
     */
    private BigDecimal amountSalary;
    /**
     * 劳务个税
     */
    private BigDecimal amountService;
    /**
     * 年终奖税
     */
    private BigDecimal amountBonus;
    /**
     * 离职金税
     */
    private BigDecimal amountLeave;
    /**
     * 股票期权税
     */
    private BigDecimal amountOption;
    /**
     * 利息、股息、红利所得税
     */
    private BigDecimal amountStock;
    /**
     * 偶然所得税
     */
    private BigDecimal amountAcc;
    /**
     * 收入额
     */
    private BigDecimal incomeTotal;
    /**
     * 免税所得
     */
    private BigDecimal incomeDutyfree;
    /**
     * 养老保险费合计_报税用
     */
    private BigDecimal deductRetirementInsurance;
    /**
     * 医疗保险费合计_报税用
     */
    private BigDecimal deductMedicalInsurance;
    /**
     * 失业保险费合计_报税用
     */
    private BigDecimal deductDlenessInsurance;
    /**
     * 住房公积金合计（报税用）
     */
    private BigDecimal deductHouseFund;
    /**
     * 允许扣除的税费
     */
    private BigDecimal deductTakeoff;
    /**
     * 企业年金个人部分
     */
    private BigDecimal annuity;
    /**
     * 商业保险
     */
    private BigDecimal businessHealthInsurance;
    /**
     * 税延养老保险费
     */
    private BigDecimal endowmentInsurance;
    /**
     * 免抵额
     */
    private BigDecimal deduction;
    /**
     * 准予扣除的捐赠额
     */
    private BigDecimal donation;
    /**
     * 减免税额
     */
    private BigDecimal taxDeduction;
    /**
     * 境内天数
     */
    private String domesticDays;
    /**
     * 境外天数
     */
    private String overseasDays;
    /**
     * 境内所得境内支付
     */
    private BigDecimal domesticIncomeDomesticPayment;
    /**
     * 境内所得境外支付
     */
    private BigDecimal domesticIncomeOverseasPayment;
    /**
     * 境外所得境内支付
     */
    private BigDecimal overseasIncomeDomesticPayment;
    /**
     * 境外所得境外支付
     */
    private BigDecimal overseasIncomeOverseasPayment;
    /**
     * 其它扣款_报税用
     */
    private BigDecimal otherDeductions;
    /**
     * 免税住房补贴
     */
    private BigDecimal housingSubsidy;
    /**
     * 免税伙食补贴
     */
    private BigDecimal mealAllowance;
    /**
     * 免税洗衣费
     */
    private BigDecimal laundryFee;
    /**
     * 免税搬迁费
     */
    private BigDecimal removingIndemnityFee;
    /**
     * 免税出差补贴
     */
    private BigDecimal missionallowance;
    /**
     * 免税探亲费
     */
    private BigDecimal visitingRelativesFee;
    /**
     * 免税语言培训费
     */
    private BigDecimal languageTrainingFee;
    /**
     * 免税子女教育经费
     */
    private BigDecimal educationFunds;
    /**
     * 年度奖金
     */
    private BigDecimal annualBonus;
    /**
     * 偶然所得
     */
    private BigDecimal fortuitousIncome;
    /**
     * 利息、股息、红利所得
     */
    private BigDecimal incomeFromInterest;
    /**
     * 劳务费
     */
    private BigDecimal serviceCharge;
    /**
     * 劳务费_允许扣除的税费
     */
    private BigDecimal serviceDeductTakeoff;
    /**
     * 离职金
     */
    private BigDecimal separationPayment;
    /**
     * 离职金免税额
     */
    private BigDecimal separationPaymentTaxFee;
    /**
     * 实际工作年限数
     */
    private String workingYears;
    /**
     * 本月行权收入
     */
    private BigDecimal exerciseIncomeMonth;
    /**
     * 本年度累计行权收入(不含本月)
     */
    private BigDecimal exerciseIncomeYear;
    /**
     * 规定月份数
     */
    private String numberOfMonths;
    /**
     * 本年累计已纳税额
     */
    private BigDecimal exerciseTaxAmount;

    //税前合计
    private BigDecimal preTaxAggregate;

    //免税津贴
    private BigDecimal dutyFreeAllowance;

    //税率
    private BigDecimal taxRate;

    //速扣数
    private BigDecimal quickCalDeduct;

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getQuickCalDeduct() {
        return quickCalDeduct;
    }

    public void setQuickCalDeduct(BigDecimal quickCalDeduct) {
        this.quickCalDeduct = quickCalDeduct;
    }

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

    public BigDecimal getAmountSalary() {
        return amountSalary;
    }

    public void setAmountSalary(BigDecimal amountSalary) {
        this.amountSalary = amountSalary;
    }

    public BigDecimal getAmountService() {
        return amountService;
    }

    public void setAmountService(BigDecimal amountService) {
        this.amountService = amountService;
    }

    public BigDecimal getAmountBonus() {
        return amountBonus;
    }

    public void setAmountBonus(BigDecimal amountBonus) {
        this.amountBonus = amountBonus;
    }

    public BigDecimal getAmountLeave() {
        return amountLeave;
    }

    public void setAmountLeave(BigDecimal amountLeave) {
        this.amountLeave = amountLeave;
    }

    public BigDecimal getAmountOption() {
        return amountOption;
    }

    public void setAmountOption(BigDecimal amountOption) {
        this.amountOption = amountOption;
    }

    public BigDecimal getAmountStock() {
        return amountStock;
    }

    public void setAmountStock(BigDecimal amountStock) {
        this.amountStock = amountStock;
    }

    public BigDecimal getAmountAcc() {
        return amountAcc;
    }

    public void setAmountAcc(BigDecimal amountAcc) {
        this.amountAcc = amountAcc;
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

    public BigDecimal getAnnuity() {
        return annuity;
    }

    public void setAnnuity(BigDecimal annuity) {
        this.annuity = annuity;
    }

    public BigDecimal getBusinessHealthInsurance() {
        return businessHealthInsurance;
    }

    public void setBusinessHealthInsurance(BigDecimal businessHealthInsurance) {
        this.businessHealthInsurance = businessHealthInsurance;
    }

    public BigDecimal getEndowmentInsurance() {
        return endowmentInsurance;
    }

    public void setEndowmentInsurance(BigDecimal endowmentInsurance) {
        this.endowmentInsurance = endowmentInsurance;
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

    public BigDecimal getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(BigDecimal taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public BigDecimal getDomesticIncomeDomesticPayment() {
        return domesticIncomeDomesticPayment;
    }

    public void setDomesticIncomeDomesticPayment(BigDecimal domesticIncomeDomesticPayment) {
        this.domesticIncomeDomesticPayment = domesticIncomeDomesticPayment;
    }

    public BigDecimal getDomesticIncomeOverseasPayment() {
        return domesticIncomeOverseasPayment;
    }

    public void setDomesticIncomeOverseasPayment(BigDecimal domesticIncomeOverseasPayment) {
        this.domesticIncomeOverseasPayment = domesticIncomeOverseasPayment;
    }

    public BigDecimal getOverseasIncomeDomesticPayment() {
        return overseasIncomeDomesticPayment;
    }

    public void setOverseasIncomeDomesticPayment(BigDecimal overseasIncomeDomesticPayment) {
        this.overseasIncomeDomesticPayment = overseasIncomeDomesticPayment;
    }

    public BigDecimal getOverseasIncomeOverseasPayment() {
        return overseasIncomeOverseasPayment;
    }

    public void setOverseasIncomeOverseasPayment(BigDecimal overseasIncomeOverseasPayment) {
        this.overseasIncomeOverseasPayment = overseasIncomeOverseasPayment;
    }

    public BigDecimal getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(BigDecimal otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public BigDecimal getHousingSubsidy() {
        return housingSubsidy;
    }

    public void setHousingSubsidy(BigDecimal housingSubsidy) {
        this.housingSubsidy = housingSubsidy;
    }

    public BigDecimal getMealAllowance() {
        return mealAllowance;
    }

    public void setMealAllowance(BigDecimal mealAllowance) {
        this.mealAllowance = mealAllowance;
    }

    public BigDecimal getLaundryFee() {
        return laundryFee;
    }

    public void setLaundryFee(BigDecimal laundryFee) {
        this.laundryFee = laundryFee;
    }

    public BigDecimal getRemovingIndemnityFee() {
        return removingIndemnityFee;
    }

    public void setRemovingIndemnityFee(BigDecimal removingIndemnityFee) {
        this.removingIndemnityFee = removingIndemnityFee;
    }

    public BigDecimal getMissionallowance() {
        return missionallowance;
    }

    public void setMissionallowance(BigDecimal missionallowance) {
        this.missionallowance = missionallowance;
    }

    public BigDecimal getVisitingRelativesFee() {
        return visitingRelativesFee;
    }

    public void setVisitingRelativesFee(BigDecimal visitingRelativesFee) {
        this.visitingRelativesFee = visitingRelativesFee;
    }

    public BigDecimal getLanguageTrainingFee() {
        return languageTrainingFee;
    }

    public void setLanguageTrainingFee(BigDecimal languageTrainingFee) {
        this.languageTrainingFee = languageTrainingFee;
    }

    public BigDecimal getEducationFunds() {
        return educationFunds;
    }

    public void setEducationFunds(BigDecimal educationFunds) {
        this.educationFunds = educationFunds;
    }

    public BigDecimal getAnnualBonus() {
        return annualBonus;
    }

    public void setAnnualBonus(BigDecimal annualBonus) {
        this.annualBonus = annualBonus;
    }

    public BigDecimal getFortuitousIncome() {
        return fortuitousIncome;
    }

    public void setFortuitousIncome(BigDecimal fortuitousIncome) {
        this.fortuitousIncome = fortuitousIncome;
    }

    public BigDecimal getIncomeFromInterest() {
        return incomeFromInterest;
    }

    public void setIncomeFromInterest(BigDecimal incomeFromInterest) {
        this.incomeFromInterest = incomeFromInterest;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getServiceDeductTakeoff() {
        return serviceDeductTakeoff;
    }

    public void setServiceDeductTakeoff(BigDecimal serviceDeductTakeoff) {
        this.serviceDeductTakeoff = serviceDeductTakeoff;
    }

    public BigDecimal getSeparationPayment() {
        return separationPayment;
    }

    public void setSeparationPayment(BigDecimal separationPayment) {
        this.separationPayment = separationPayment;
    }

    public BigDecimal getSeparationPaymentTaxFee() {
        return separationPaymentTaxFee;
    }

    public void setSeparationPaymentTaxFee(BigDecimal separationPaymentTaxFee) {
        this.separationPaymentTaxFee = separationPaymentTaxFee;
    }

    public BigDecimal getExerciseIncomeMonth() {
        return exerciseIncomeMonth;
    }

    public void setExerciseIncomeMonth(BigDecimal exerciseIncomeMonth) {
        this.exerciseIncomeMonth = exerciseIncomeMonth;
    }

    public BigDecimal getExerciseIncomeYear() {
        return exerciseIncomeYear;
    }

    public void setExerciseIncomeYear(BigDecimal exerciseIncomeYear) {
        this.exerciseIncomeYear = exerciseIncomeYear;
    }

    public BigDecimal getExerciseTaxAmount() {
        return exerciseTaxAmount;
    }

    public void setExerciseTaxAmount(BigDecimal exerciseTaxAmount) {
        this.exerciseTaxAmount = exerciseTaxAmount;
    }

    public String getDomesticDays() {
        return domesticDays;
    }

    public void setDomesticDays(String domesticDays) {
        this.domesticDays = domesticDays;
    }

    public String getOverseasDays() {
        return overseasDays;
    }

    public void setOverseasDays(String overseasDays) {
        this.overseasDays = overseasDays;
    }

    public String getWorkingYears() {
        return workingYears;
    }

    public void setWorkingYears(String workingYears) {
        this.workingYears = workingYears;
    }

    public String getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(String numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }
}

























