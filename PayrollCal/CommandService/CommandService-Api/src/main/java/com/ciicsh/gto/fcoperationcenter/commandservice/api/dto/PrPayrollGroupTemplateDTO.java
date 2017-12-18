package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 配置表，薪资组模板表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollGroupTemplateDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 自增薪资组ID
     */
	private Integer id;
    /**
     * 薪资组模板编码，规则为：XZZMB-xxxxxx(六位数字序号)
     */
	private String groupTemplateCode;
    /**
     * 薪资组模板名称
     */
	private String groupTemplateName;
    /**
     * 版本号
     */
	private String version;
    /**
     * 审核状态：
        1 审核中；
        2 审核完成；
        3 拒绝；
     */
	private Integer approvalStatus;
    /**
     * 审核状态Label：
     */
    private String approvalStatusLabel;
    /**
     * 审核意见
     */
	private String comments;
    /**
     * 备注
     */
	private String remark;
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
