package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 配置表，雇员组表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrEmpGroupDTO{

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 雇员组编码，规则为：GYZ-客户ID-xxx(三位数字序号)
     */
	private String empGroupCode;
    /**
     * 所属管理方ID
     */
	private String managementId;
    /**
     * 管理方名称
     */
	private String managementName;
    /**
     * 雇员组名称
     */
	private String name;
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
