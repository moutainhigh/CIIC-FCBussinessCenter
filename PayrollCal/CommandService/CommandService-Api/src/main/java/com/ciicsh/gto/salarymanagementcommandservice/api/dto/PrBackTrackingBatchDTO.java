package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * <p>
 * 回溯批次主表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrBackTrackingBatchDTO{

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 参考批次，即被回溯的批次ID
     */
	private Integer originBatchId;
    /**
     * 回溯批次编号： B-客户ID-计算日期-4位序号
     */
	private String backTrackingBatchCode;
    /**
     * 所属管理方ID
     */
	private String managementId;
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
     * 回溯调整雇员薪资字段JSON格式（用于记录回溯调整计算前雇员的变更字段值）
     */
	private String backEmpAdjustFields;
    /**
     * 回溯调整雇员特殊计算项字段：雇员ID，雇员个税合计，雇员实际发工资
     */
	private String backEmpSpecFields;
    /**
     * 回溯调整计算结果
     */
	private String backEmpResult;
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
