package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放导出雇员信息
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-15
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
@ExcelTarget("SalaryTaskEmpExcel")
public class SalaryTaskEmpExcelDTO implements Serializable {
    @Excel(name = "雇员编号")
    private String employeeId;
    @Excel(name = "雇员名称")
    private String employeeName;
    @Excel(name = "薪资周期")
    private String grantCycle;
    @Excel(name = "薪酬计算批次号")
    private String batchCode;
    @Excel(name = "发放账户")
    private String grantAccountCode;
    @Excel(name = "收款人账号")
    private String cardNum;
    @Excel(name = "收款人姓名")
    private String accountName;
    @Excel(name = "收款行行号")
    private String bankCode;
    @Excel(name = "收款行名称")
    private String depositBank;
    @Excel(name = "发放金额")
    private BigDecimal paymentAmount;
    @Excel(name = "发放币种")
    private String currencyName;
    @Excel(name = "发放状态")
    private String grantStatusName;
    @Excel(name = "国籍")
    private String countryName;
    @Excel(name = "备注")
    private String remark;
}
