package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 配置表，公式配置表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrFormulasDTO{

    private static final long serialVersionUID = 1L;

    /**
     * 公式ID
     */
	private Integer id;
    /**
     * 薪资项ID
     */
	private Integer itemId;
    /**
     * 公式内容
     */
	private String content;
    /**
     * 公式说明
     */
	private String desc;
    /**
     * 公式条件
     */
	private String condition;
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
