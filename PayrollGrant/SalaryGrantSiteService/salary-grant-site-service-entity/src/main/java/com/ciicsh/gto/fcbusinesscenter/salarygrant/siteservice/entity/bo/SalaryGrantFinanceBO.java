package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 财务报表
 * </p>
 *
 * @author chenpb
 * @since 2018-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantFinanceBO {
    /**
     * 任务单
     */
    private FinanceTaskBO task;
    /**
     * 任务单ID
     */
    private Long taskId;
    /**
     * 流水号
     */
    private String taskCode;
    /**
     * 发放批次
     */
    private String batchCode;
    /**
     * 个税期间
     */
    private String taxCycle;
    /**
     * 公司编号
     */
    private String managementId;
    /**
     * 公司名称
     */
    private String managementName;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 打印日期
     */
    private Date printDate;
    /**
     * 发放人数
     */
    private Integer amountNum = 0;
    /**
     * 年终奖人数
     */
    private Integer yearBonusNum = 0;
    /**
     * 操作员
     */
    private String operatorUserId;
    /**
     * 审核员
     */
    private String approveUserId;
    /**
     * 雇员列表
     */
    private List<FinanceEmployeeBO> empList;
}
