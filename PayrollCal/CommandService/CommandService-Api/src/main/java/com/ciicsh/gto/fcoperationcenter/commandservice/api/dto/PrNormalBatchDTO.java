package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 正常批次主表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrNormalBatchDTO{

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
	private Long id;
    /**
     * 正常批次编号： 客户ID-计算日期-4位序号
     */
	private String code;
    /**
     * 所属管理方ID
     */
	private String managementId;

    /**
     * 所属管理方名称
     */
	private String managementName;

    /**
     * 是否计划批次
     */
	private Boolean isScheduled;
    /**
     * 计划批次设置, JSON格式字符串
     */
	private String scheduleSetting;
    /**
     * 薪资账套Code
     */
	private String accountSetCode;
    /**
     * 备注
     */
	private String remark;
    /**
     * 批次状态：
1-新建
2-计算中
3-计算完成
4-审核中
5-审核完成
6-关账
7-已发放
8-个税已申报
     */
	private Integer status;
    /**
     * 薪资期间 年/月
     */
	private String period;

    /**
     * 实际记薪年月
     */
	private String actualPeriod;

    /**
     * 薪资计算结果
     */
	private String resultData;
    /**
     * 是否实施计算
     */
	private Boolean isImplCal;
    /**
     * 是否有效
     */
	private Boolean isActive;
    /**
     * 是否来款
     */
	private Boolean hasMoney;
    /**
     * 数据创建时间
     */
	private Date createdTime;
    /**
     * 最后修改时间
     */
	private Date modifiedTime;
    /**
     * 创建人
     */
	private String createdBy;
    /**
     * 最后修改人
     */
	private String modifiedBy;
}
