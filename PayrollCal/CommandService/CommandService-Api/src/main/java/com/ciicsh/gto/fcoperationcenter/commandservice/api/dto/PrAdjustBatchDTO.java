package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 调整批次主表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
public class PrAdjustBatchDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长ID
     */
	private Long id;
    /**
     * 参考批次，即被调整的正常批次
     */
	private Integer originBatchId;
    /**
     * 调整批次编号： A-客户ID-计算日期-4位序号
     */
	private String adjustBatchCode;
    /**
     * 所属管理方ID
     */

	private String managementId;
    /**
     * 是否计划批次
     */

	private Boolean isScheduled;
    /**
     * 计划批次设置, JSON格式字符串
     */
	private String scheduleSetting;
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
     * 调整后薪资计算结果
     */
	private String adjustResult;
    /**
     * 是否有效
     */
	private Boolean isActive;
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
