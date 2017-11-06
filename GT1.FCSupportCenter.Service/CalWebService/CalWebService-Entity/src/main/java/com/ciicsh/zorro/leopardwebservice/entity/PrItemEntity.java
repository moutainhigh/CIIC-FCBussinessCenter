package com.ciicsh.zorro.leopardwebservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Integer prItemId;

    /**
     * 薪资项编码
     */
    private String prItemcode;

    /**
     * 所属薪酬模版Id
     */
    private String templateId;

    /**
     * 管理方ID
     */
    private String managementId;

    /**
     * 客户ID
     */
    private String companyId;

    /**
     * 所属薪资组ID
     */
    private String prGroupId;

    /**
     * 薪资项名称
     */
    private String name;

    /**
     * 薪资项名称-计算批次别名
     */
    private String batchAliasName;

    /**
     * 薪资项名称-工资单别称
     */
    private String payrollAliasName;

    /**
     * 薪资项名称-报表别称
     */
    private String reportAliasName;

    /**
     * 薪资项类型
     */
    private Integer type;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 默认值类型
     */
    private String defaultValueStyle;

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
     * 备注
     */
    private String prItemRemark;

    /**
     * 计算优先级
     */
    private Integer priority;

    /**
     * 薪资项值
     */
    private Object itemValue;

    /**
     * 解析后的薪资项公式条件
     */
    private String resolvedCondition;
}
