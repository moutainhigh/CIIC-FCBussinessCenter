package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 配置表，函数配置表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrFunctionsDTO implements Serializable{

    private static final long serialVersionUID = -8479029352436182324L;
    /**
     * 函数ID
     */
	private Integer id;
    /**
     * 函数名称, 显示于计算项公式设计器里
     */
	private String name;
    /**
     * 规则名称
     */
	private String ruleName;
    /**
     * 函数大类
     */
	private String category;
    /**
     * 函数小类
     */
	private String subCateogry;
    /**
     * DRL文件名，一个DRL一个函数
     */
	private String drlFile;
    /**
     * 形参集合
     */
	private String paramSet;
    /**
     * 备注说明
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
