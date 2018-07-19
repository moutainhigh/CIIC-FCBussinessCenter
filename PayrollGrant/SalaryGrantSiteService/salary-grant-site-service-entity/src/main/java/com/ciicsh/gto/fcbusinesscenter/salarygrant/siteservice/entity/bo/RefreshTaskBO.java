package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 待提交任务单信息
 * </p>
 *
 * @author chenpb
 * @since 2018-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RefreshTaskBO {
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
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
    private String grantMode;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放
     */
    private Integer grantType;
    /**
     * 任务单类型
     */
    private Integer taskType;
    /**
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待支付，6-未支付，7-已支付，8-撤回，9-驳回
     */
    private String taskStatus;
    /**
     * 雇员列表
     */
    private List<SalaryGrantEmployeePO> empList;
}
