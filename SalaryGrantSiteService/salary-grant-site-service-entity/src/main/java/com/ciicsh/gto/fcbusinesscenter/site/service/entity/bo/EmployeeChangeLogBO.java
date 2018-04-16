package com.ciicsh.gto.fcbusinesscenter.site.service.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 雇员变更日志信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-02-28
 */
public class EmployeeChangeLogBO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 雇员中智终身编号
     */
    private String employeeId;
    /**
     * 薪资发放任务单主表编号
     */
    private String salaryGrantMainTaskCode;
    /**
     * 薪资发放任务单子表编号
     */
    private String salaryGrantSubTaskCode;
    /**
     * 变更表名
     */
    private String changeTableName;
    /**
     * 变更主键id
     */
    private Long changeTableId;
    /**
     * 变更字段
     */
    private String changeField;
    /**
     * 修改值
     */
    private String changeValue;
    /**
     * 变更类型
     */
    private String changeType;
    /**
     * 更改操作:0-修改、1-删除、2-新增
     */
    private Integer changeOperation;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * 雇员信息编号
     */
    private Long salaryGrantEmployeeId;
    /**
     * 发放账户编号
     */
    private String grantAccountCode;
    /**
     * 发放方式
     */
    private Integer grantMode;
    /**
     * 雇员信息编号
     */
    private Long salaryGrantRuleId;

    /**
     * 银行卡编号
     */
    private Long bankcardId;
    /**
     * 收款人账号
     */
    private String cardNum;
    /**
     * 收款人姓名
     */
    private String accountName;
    /**
     * 收款行行号
     */
    private String bankCode;
    /**
     * 收款行名称
     */
    private String depositBank;
    /**
     * 银行国际代码
     */
    private String swiftCode;
    /**
     * 国际银行账户号码
     */
    private String iban;
    /**
     * 银行卡种类
     */
    private Integer bankcardType;
    /**
     * 国家代码
     */
    private String countryCode;
    /**
     * 变更日志
     */
    private String changeLog;
    /**
     * 任务单创建时间
     */
    private Date createdTime;
    /**
     * 雇员备注信息
     */
    private String empRemark;
    /**
     * 任务单备注信息
     */
    private String taskRemark;
    /**
     * 人民币金额
     */
    private BigDecimal paymentAmountRMB;
    /**
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 公司编号
     */
    private String companyId;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSalaryGrantMainTaskCode() {
        return salaryGrantMainTaskCode;
    }

    public void setSalaryGrantMainTaskCode(String salaryGrantMainTaskCode) {
        this.salaryGrantMainTaskCode = salaryGrantMainTaskCode;
    }

    public String getSalaryGrantSubTaskCode() {
        return salaryGrantSubTaskCode;
    }

    public void setSalaryGrantSubTaskCode(String salaryGrantSubTaskCode) {
        this.salaryGrantSubTaskCode = salaryGrantSubTaskCode;
    }

    public String getGrantAccountCode() {
        return grantAccountCode;
    }

    public void setGrantAccountCode(String grantAccountCode) {
        this.grantAccountCode = grantAccountCode;
    }

    public Integer getGrantMode() {
        return grantMode;
    }

    public void setGrantMode(Integer grantMode) {
        this.grantMode = grantMode;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Integer getBankcardType() {
        return bankcardType;
    }

    public void setBankcardType(Integer bankcardType) {
        this.bankcardType = bankcardType;
    }

    public String getChangeTableName() {
        return changeTableName;
    }

    public void setChangeTableName(String changeTableName) {
        this.changeTableName = changeTableName;
    }

    public Long getChangeTableId() {
        return changeTableId;
    }

    public void setChangeTableId(Long changeTableId) {
        this.changeTableId = changeTableId;
    }

    public String getChangeField() {
        return changeField;
    }

    public void setChangeField(String changeField) {
        this.changeField = changeField;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Integer getChangeOperation() {
        return changeOperation;
    }

    public void setChangeOperation(Integer changeOperation) {
        this.changeOperation = changeOperation;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getSalaryGrantEmployeeId() {
        return salaryGrantEmployeeId;
    }

    public void setSalaryGrantEmployeeId(Long salaryGrantEmployeeId) {
        this.salaryGrantEmployeeId = salaryGrantEmployeeId;
    }

    public Long getSalaryGrantRuleId() {
        return salaryGrantRuleId;
    }

    public void setSalaryGrantRuleId(Long salaryGrantRuleId) {
        this.salaryGrantRuleId = salaryGrantRuleId;
    }

    public Long getBankcardId() {
        return bankcardId;
    }

    public void setBankcardId(Long bankcardId) {
        this.bankcardId = bankcardId;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getEmpRemark() {
        return empRemark;
    }

    public void setEmpRemark(String empRemark) {
        this.empRemark = empRemark;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public BigDecimal getPaymentAmountRMB() {
        return paymentAmountRMB;
    }

    public void setPaymentAmountRMB(BigDecimal paymentAmountRMB) {
        this.paymentAmountRMB = paymentAmountRMB;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
