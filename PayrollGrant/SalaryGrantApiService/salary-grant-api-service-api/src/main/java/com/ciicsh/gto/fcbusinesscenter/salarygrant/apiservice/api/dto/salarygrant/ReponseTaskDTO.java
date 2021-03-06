package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReponseTaskDTO {
    private static final long serialVersionUID = 1L;
    /**
     * 任务单ID
     */
    private Long taskId;
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 管理方编号
     */
    private String managementId;
    /**
     * 管理方名称
     */
    private String managementName;
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 薪资发放总金额（RMB）
     */
    private BigDecimal paymentTotalSum;
    /**
     * 发薪人数
     */
    private Integer totalPersonCount;
    /**
     * 中方发薪人数
     */
    private Integer chineseCount;
    /**
     * 外方发薪人数
     */
    private Integer foreignerCount;
    /**
     * 薪资发放日期
     */
    private String grantDate;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放，6-现金
     */
    private Integer grantType;
    /**
     * 发放类型名称
     */
    private String grantTypeName;
    /**
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待生成，6-未支付，7-已支付，8-撤回，9-驳回，13-作废
     */
    private String taskStatus;
    /**
     * 状态中文描述
     */
    private String taskStatusName;
}
