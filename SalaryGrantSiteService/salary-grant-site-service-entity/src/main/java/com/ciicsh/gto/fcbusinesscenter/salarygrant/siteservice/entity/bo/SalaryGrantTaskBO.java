package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 薪资发放
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
public class SalaryGrantTaskBO extends PagingBO implements Serializable {
    /**
     * 任务单ID
     */
    private Long taskId;
    /**
     * 主任务单编号
     */
    private String mainTaskCode;
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 管理方编号
     */
    private String managementId;
    /**
     * 管理方编号列表
     */
    private String managementIds;
    /**
     * 管理方名称
     */
    private String managementName;
    /**
     * 薪酬计算批次号
     */
    private String batchCode;
    /**
     * 发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
     */
    private String grantMode;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 发放状态
     */
    private Integer grantStatus;
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
     * 中智上海发薪人数
     */
    private Integer localGrantCount;
    /**
     * 中智代发（委托机构）发薪人数
     */
    private Integer supplierGrantCount;
    /**
     * 薪资发放日期
     */
    private String grantDate;
    /**
     * 薪资发放时段:1-上午，2-下午
     */
    private Integer grantTime;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放
     */
    private Integer grantType;
    /**
     * 发放类型名称
     */
    private String grantTypeName;
    /**
     * 发放方式名称
     */
    private String grantModeName;
    /**
     * 发放账户编号
     */
    private String grantAccountCode;
    /**
     * 发放账户名称
     */
    private String grantAccountName;
    /**
     * 任务单类型
     */
    private Integer taskType;
    /**
     * 备注:任务单中雇员信息变化提示链接
     */
    private String remark;
    /**
     * 失效原因
     */
    private String invalidReason;
    /**
     * 审批意见
     */
    private String approvedOpinion;
    /**
     * 状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效，5-待支付，6-未支付，7-已支付，8-撤回，9-驳回
     */
    private String taskStatus;
    /**
     * 状态中文描述
     */
    private String taskStatusName;
    /**
     * 状态英文名
     */
    private String taskStatusEn;
    /**
     * 操作员
     */
    private String operateUser;
    /**
     * 最后修改时间
     */
    private Date operateDate;
    /**
     * 操作
     */
    private String operateStep;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * 当前登录用户ID
     */
    private String currentUserId;
    /**
     * 系统用户ID
     */
    private String userId;
    /**
     * 系统用户名
     */
    private String userName;
    /**
     * 创建时间字符串
     */
    private String createdTimeStr;
    /**
     * 操作员
     */
    private String operatorUserId;
    /**
     * 审核员
     */
    private String approveUserId;
    /**
     * 结算发放标识:0-正常，1-垫付
     */
    private Integer balanceGrant;
    /**
     * 垫付类型:1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付
     */
    private Integer advanceType;
    /**
     * 版本号
     */
    private Long batchVersion;
}
