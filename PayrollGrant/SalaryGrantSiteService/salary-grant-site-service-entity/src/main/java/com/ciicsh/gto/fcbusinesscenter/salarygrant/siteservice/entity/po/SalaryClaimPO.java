package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po;

import java.io.Serializable;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 薪资发放工资认领信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-04
 */
@TableName("sg_salary_claim")
public class SalaryClaimPO extends Model<SalaryClaimPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务单编号
     */
    @TableId("task_code")
    private String taskCode;
    /**
     * 收款方式:1-中智上海
     */
    @TableField("collecting_mode")
    private Integer collectingMode;
    /**
     * 序号
     */
    @TableField("serial_number")
    private String serialNumber;
    /**
     * 日期
     */
    @TableField("claim_date")
    private String claimDate;
    /**
     * 来款金额
     */
    @TableField("receive_amount")
    private BigDecimal receiveAmount;
    /**
     * 币种
     */
    @TableField("currency_code")
    private String currencyCode;
    /**
     * 摘要
     */
    @TableField("claim_abstract")
    private String claimAbstract;
    /**
     * 认领金额
     */
    @TableField("claim_amount")
    private BigDecimal claimAmount;
    /**
     * receive_account_code
     */
    @TableField("receive_account_code")
    private String receiveAccountCode;
    /**
     * 对方账户名称
     */
    @TableField("receive_account_name")
    private String receiveAccountName;
    /**
     * 工资认领备注
     */
    private String remark;


    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Integer getCollectingMode() {
        return collectingMode;
    }

    public void setCollectingMode(Integer collectingMode) {
        this.collectingMode = collectingMode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getClaimAbstract() {
        return claimAbstract;
    }

    public void setClaimAbstract(String claimAbstract) {
        this.claimAbstract = claimAbstract;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getReceiveAccountCode() {
        return receiveAccountCode;
    }

    public void setReceiveAccountCode(String receiveAccountCode) {
        this.receiveAccountCode = receiveAccountCode;
    }

    public String getReceiveAccountName() {
        return receiveAccountName;
    }

    public void setReceiveAccountName(String receiveAccountName) {
        this.receiveAccountName = receiveAccountName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.taskCode;
    }

    @Override
    public String toString() {
        return "SalaryClaimPO{" +
                ", taskCode=" + taskCode +
                ", collectingMode=" + collectingMode +
                ", serialNumber=" + serialNumber +
                ", claimDate=" + claimDate +
                ", receiveAmount=" + receiveAmount +
                ", currencyCode=" + currencyCode +
                ", claimAbstract=" + claimAbstract +
                ", claimAmount=" + claimAmount +
                ", receiveAccountCode=" + receiveAccountCode +
                ", receiveAccountName=" + receiveAccountName +
                ", remark=" + remark +
                "}";
    }
}
