package com.ciicsh.zorro.leopardwebservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by jiangtianning on 2017/10/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrItemTemplateEntity extends PrBaseEntity implements Serializable{

    /**
     * entityId
     */
    private String entityId;

    /**
     * 管理方ID
     */
    private String managementId;

    /**
     * 薪资项模板编码
     */
    private String code;

    /**
     * 薪资项模板名
     */
    private String name;

    /**
     * 薪资项模板类型
     */
    private Integer type;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 是否使用高级公式
     */
    private Boolean ifComplex;

    /**
     * 条件
     */
    private String condition;

    /**
     * 解析后条件
     */
    private String resolvedCondition;

    /**
     * 公式
     */
    private String formula;

    /**
     * 备注
     */
    private String remark;

}
