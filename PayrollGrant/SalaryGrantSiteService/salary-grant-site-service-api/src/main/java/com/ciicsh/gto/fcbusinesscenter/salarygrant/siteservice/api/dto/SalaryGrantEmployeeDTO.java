package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放雇员信息表 DTO
 * </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/26 0026
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantEmployeeDTO extends PagingDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 雇员信息编号
     */
    private Long salaryGrantEmployeeId;
    /**
     * 任务单类型
     */
    private Integer taskType;
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 薪资发放任务单子表编号
     */
    private String salaryGrantSubTaskCode;
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
     * 发放账户名称
     */
    private String grantAccountName;
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
     * 发放币种:CNY-人民币，USD-美元，EUR-欧元
     */
    private String currencyCode;
    /**
     * 发放币种名称
     */
    private String currencyName;
    /**
     * 发放状态:0-正常，1-手动暂缓，2-自动暂缓，3-退票，4-部分发放
     */
    private Integer grantStatus;
    /**
     * 发放状态中文
     */
    private String grantStatusName;
    /**
     * 国籍
     */
    private String countryCode;
    /**
     * 国籍中文
     */
    private String countryName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 调整信息
     */
    private String adjustInfo;
    /**
     * 信息调整日期字符串
     */
    private String adjustInfoDateStr;
}
