package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 配置表，薪资项明细表
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PrPayrollItemDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 薪资项Id
     */
    private Integer id;
    /**
     * 薪资项编码, 规则为XZX-9位管理方ID-薪资项类型缩写-3位数字序号
     */
    private String itemCode;
    /**
     * 继承薪资组模板薪资项Code: 非必选项，让用户可以不需模板直接产生薪资项。
     */
    private String parentItemCode;
    /**
     * 所属管理方ID
     */
    private String managementId;
    /**
     * 薪资组编码
     */
    private String payrollGroupCode;
    /**
     * 薪资组模版编码
     */
    private String payrollGroupTemplateCode;
    /**
     * 薪资项名称
     */
    private String itemName;
    /**
     * 薪资项类型：
     * 1 - 固定输入项，一次初始化，很少变更，建立批次时一般直接使用
     * 2 - 可变输入项，每建立一个批次，需要从FC客户中心服务接口导入值
     * 3-计算项，通过配置好的公式计算得出的薪资项，如：加班工资，病假工资等
     */
    private Integer itemType;
    /**
     * 薪资项类型名称
     */
    private String itemTypeLabel;
    /**
     * 基类薪资项Code
     */
    private String baseItemCode;
    /**
     * 数据格式: 1-文本,2-数字,3-日期,4-布尔
     */
    private Integer dataType;
    /**
     * 数据格式名称: 1-文本,2-数字,3-日期,4-布尔
     */
    private String dataTypeLabel;
    /**
     * 默认值处理方式：1 - 使用历史数据 2 - 使用基本值
     */
    private Integer defaultValueStyle;
    /**
     * 默认值处理方式名称
     */
    private String defaultValueStyleLabel;
    /**
     * 默认数字
     */
    private String defaultValue;
    /**
     * 小数处理方式：
     * 1 - 四舍五入
     * 2 - 简单去位
     */
    private Integer decimalProcessType;
    /**
     * 小数处理方式名称
     */
    private String decimalProcessTypeLabel;
    /**
     * 计算精度
     */
    private Integer calPrecision;
    /**
     * 原始完整公式结构体
     */
    private String fullFormula;
    /**
     * 公式原始内容
     */
    private String originFormula;
    /**
     * 公式内容
     */
    private String formulaContent;
    /**
     * 条件原始输入值
     */
    private String originCondition;
    /**
     * 薪资项取值范围的条件描述
     */
    private String itemCondition;
    /**
     * 计算顺序
     */
    private Integer calPriority;
    /**
     * 显示顺序
     */
    private Integer displayPriority;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否可以上锁
     */
    private Boolean canLock;
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
    /**
     * 扩展标识：1-扩展字段；0-非扩展字段
     */
    private Integer extendFlag;
}
