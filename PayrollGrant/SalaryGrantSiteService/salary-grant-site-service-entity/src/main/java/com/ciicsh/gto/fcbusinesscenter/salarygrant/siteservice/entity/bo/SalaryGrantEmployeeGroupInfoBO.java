package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>Description: 薪资发放雇员分组信息BO</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/15 0015
 */
@Data
public class SalaryGrantEmployeeGroupInfoBO {
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 银行卡种类 =>1：建行，2：工行，3：招商银行，4：中国银行，5：其他银行
     */
    private Integer bankcardType;
    /**
     * 付款账号
     */
    private String paymentAccountCode;
    /**
     * 付款账户名
     */
    private String paymentAccountName;
    /**
     * 付款银行名
     */
    private String paymentAccountBankName;
    /**
     * 发放币种:CNY-人民币，USD-美元，EUR-欧元
     */
    private String currencyCode;
    /**
     * 发薪人数合计
     */
    private Long paymentCount;
    /**
     * 发放金额合计
     */
    private BigDecimal paymentAmountSum;
    /**
     * 薪资发放雇员信息表记录List
     */
    private List<SalaryGrantEmployeePO> salaryGrantEmployeePOList;
}
