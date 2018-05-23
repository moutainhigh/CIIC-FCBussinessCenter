package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 薪资发放详情
 * </p>
 *
 * @author chenpeb
 * @since 2018-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantDetailDTO {
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
     * 任务单类型
     */
    private Integer taskType;
    /**
     * 任务单状态
     */
    private String taskStatus;
    /**
     * 薪资周期
     */
    private String grantCycle;
    /**
     * 发放类型:1-正常发放，2-调整发放，3-回溯发放，4-暂缓再发放，5-退票发放
     */
    private Integer grantType;
    /**
     * 薪资发放日期
     */
    private String grantDate;
    /**
     * 薪资发放时段
     */
    private Integer grantTime;
    /**
     * 薪资发放时段
     */
    private String grantTimeStr;
    /**
     * 发放方式
     */
    private String grantMode;
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
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdTime;

}
