package com.ciicsh.gto.salarymanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jiangtianning on 2017/10/24.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrItemEntity extends PrBaseEntity{

    /**
     * 薪资项entityId
     */
    private String entityId;

    /**
     * 薪资项编码
     */
    private String code;

    /**
     * 所属薪酬模版Id
     */
    private String templateId;

    /**
     * 管理方ID
     */
    private String managementId;

    /**
     * 所属薪资组ID
     */
    private String prGroupId;

    /**
     * 所属薪资组模板ID
     */
    private String prGroupTemplateId;

    /**
     * 薪资项名称
     */
    private String name;

    /**
     * 薪资项别名
     */
    private String aliasName;

    /**
     * 薪资项类型
     */
    private Integer type;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 默认值类型
     */
    private Integer defaultValueStyle;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 小数处理方式
     */
    private Integer decimalProcessType;

    /**
     * 计算精度
     */
    private Integer precision;

    /**
     * 是否绑定模板
     */
    private Boolean isBindingWithTemplate;

    /**
     * 公式内容
     */
    private String formula;

    /**
     * 取值范围描述
     */
    private String itemCondition;

    /**
     * 是否显示
     */
    private Boolean isHidden;

    /**
     * 显示优先级
     */
    private Integer disPriority;

    /**
     * 计算优先级
     */
    private Integer calPriority;

    /**
     * 薪资项值
     */
    private Object itemValue;

    /**
     * 解析后的薪资项公式条件
     */
    private String resolvedCondition;

    /**
     * 备注
     */
    private String remark;
}
