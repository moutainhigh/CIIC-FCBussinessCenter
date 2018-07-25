package com.ciicsh.gto.salarymanagement.entity.po;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 审批通过的薪资项明细表
 * </p>
 *
 * @author baofeng@ciicsh.com
 * @since 2018-7-18 10:52:14
 */
@Getter
@Setter
@ToString
@TableName("pr_approved_payroll_item")
public class PrApprovedPayrollItemPO extends Model<PrApprovedPayrollItemPO> implements Serializable {

    private static final long serialVersionUID = 482246221031889717L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 薪资项编码, 规则为XZX-9位管理方ID-薪资项类型缩写-3位数字序号
     */
    @TableField("item_code")
    private String itemCode;
    /**
     * 继承薪资组模板薪资项Code: 非必选项，让用户可以不需模板直接产生薪资项。
     */
    @TableField("parent_item_code")
    private String parentItemCode;
    /**
     * 所属管理方ID
     */
    @TableField("management_id")
    private String managementId;
    /**
     * 薪资组编码
     */
    @TableField("payroll_group_code")
    private String payrollGroupCode;
    /**
     * 薪资组模版编码
     */
    @TableField("payroll_group_template_code")
    private String payrollGroupTemplateCode;
    /**
     * 薪资项名称
     */
    @TableField("item_name")
    private String itemName;
    /**
     * 薪资项类型：
     * 1 - 固定输入项，一次初始化，很少变更，建立批次时一般直接使用
     * 2 - 可变输入项，每建立一个批次，需要从FC客户中心服务接口导入值
     * 3-计算项，通过配置好的公式计算得出的薪资项，如：加班工资，病假工资等
     */
    @TableField("item_type")
    private Integer itemType;
    /**
     * 基类薪资项Code
     */
    @TableField("base_item_code")
    private String baseItemCode;
    /**
     * 数据格式: 1-文本,2-数字,3-日期,4-布尔
     */
    @TableField("data_type")
    private Integer dataType;
    /**
     * 默认值处理方式：
     * 1 - 使用历史数据
     * 2 - 使用基本值
     */
    @TableField("default_value_style")
    private Integer defaultValueStyle;
    /**
     * 默认数字
     */
    @TableField("default_value")
    private String defaultValue;
    /**
     * 小数处理方式：
     * 1 - 四舍五入
     * 2 - 简单去位
     */
    @TableField("decimal_process_type")
    private Integer decimalProcessType;
    /**
     * 计算精度
     */
    @TableField("cal_precision")
    private Integer calPrecision;
    /**
     * 公式原始内容
     */
    @TableField("origin_formula")
    private String originFormula;
    /**
     * 公式内容
     */
    @TableField("formula_content")
    private String formulaContent;
    /**
     * 薪资项取值范围的条件描述
     */
    @TableField("item_condition")
    private String itemCondition;
    /**
     * 计算顺序
     */
    @TableField("cal_priority")
    private Integer calPriority;
    /**
     * 显示顺序
     */
    @TableField("display_priority")
    private Integer displayPriority;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 是否可以上锁
     */
    @TableField("can_lock")
    private Boolean canLock;
    /**
     * 是否有效
     */
    @TableField("is_active")
    private Boolean isActive;
    /**
     * 数据创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 最后修改时间
     */
    @TableField("modified_time")
    private Date modifiedTime;
    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;
    /**
     * 最后修改人
     */
    @TableField("modified_by")
    private String modifiedBy;

    /**
     * 薪资项值用于计算扩展
     */
    @TableField("item_value")
    private String itemValue;

    /**
     * 条件原始输入值
     */
    @TableField("origin_condition")
    private String originCondition;

    /**
     * 完整公式结构体
     */
    @TableField("full_formula")
    private String fullFormula;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
