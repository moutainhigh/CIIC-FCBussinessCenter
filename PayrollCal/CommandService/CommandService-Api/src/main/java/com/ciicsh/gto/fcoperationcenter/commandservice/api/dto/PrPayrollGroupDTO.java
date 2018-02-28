package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 配置表，薪资组表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollGroupDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
	private Integer id;
    /**
     * 薪资组编码，规则为：XZZ-客户ID-xxx(三位数字序号)
     */
	private String groupCode;
    /**
     * 所属管理方ID
     */
	private String managementId;
    /**
     * 管理方名
     */
	private String managementLabel;
    /**
     * 继承薪资组模板ID
     */
	private String groupTemplateCode;
    /**
     * 薪资组名称
     */
	private String groupName;
    /**
     * 版本号
     */
	private String version;
    /**
     * 备注
     */
	private String remark;
    /**
     * 是否是薪资组模板 - 管理方和客户都是财务公司（FC）时默认为是
     */
	private Boolean isTemplate;
    /**
     * 审核状态：
1 审核中；
2 审核通过；
3 拒绝；
     */
	private Integer approvalStatus;

    /**
     * 审核状态名
     */
    private String approvalStatusLabel;
    /**
     * 审核意见
     */
	private String comments;
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
