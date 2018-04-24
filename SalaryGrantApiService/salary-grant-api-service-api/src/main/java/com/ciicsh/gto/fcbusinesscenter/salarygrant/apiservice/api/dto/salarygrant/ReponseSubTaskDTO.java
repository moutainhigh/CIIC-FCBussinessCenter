package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 薪资发放任务单子表请求查询结果
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReponseSubTaskDTO {
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
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
    private Integer grantMode;
    /**
     * 发放方式名称
     */
    private String grantModeName;
    /**
     * 发放账户编号
     */
    private String grantAccountCode;
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
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待支付，6-未支付，7-已支付，8-撤回，9-驳回，10-待合并，11-已合并，12-已确认
     */
    private String taskStatus;
    /**
     * 状态中文描述
     */
    private String taskStatusName;
}
