package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 业务编码自增序列
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrCodeSequenceDTO {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 序列名
     */
	private String seqName;
    /**
     * 前缀
     */
	private String prefix;
    /**
     * 当前序列值
     */
	private Long currentVal;
    /**
     * 增幅
     */
	private Long incrementVal;
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
