package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 财务报表任务单信息
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FinanceTaskBO {
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
}
