package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;
import lombok.*;

import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单
 * </p>
 *
 * @author chenpb
 * @since 2018-04-20
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantEmpDTO extends PagingDTO {
    /**
     * 雇员信息编号
     */
    private Long salaryGrantEmployeeId;
    /**
     * 雇员编号
     */
    private String employeeId;
    /**
     * 雇员名称
     */
    private String employeeName;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 发放账户编号
     */
    private String grantAccountCode;
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
     * 发放金额
     */
    private BigDecimal paymentAmount;
    /**
     * 发放金额折合人民币
     */
    private BigDecimal paymentAmountForRMB;
    /**
     * 发放币种名称
     */
    private String currencyName;
    /**
     * 国籍中文
     */
    private String countryName;
    /**
     * 发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
     */
    private Integer grantStatus;
    /**
     * 发放状态中文
     */
    private String grantStatusName;
    /**
     * 备注
     */
    private String remark;
}
