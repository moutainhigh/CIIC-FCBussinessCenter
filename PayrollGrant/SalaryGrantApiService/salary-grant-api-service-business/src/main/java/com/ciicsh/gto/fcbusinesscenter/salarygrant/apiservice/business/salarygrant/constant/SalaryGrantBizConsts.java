package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.constant;

public class SalaryGrantBizConsts {
    /** 发放服务标识：2-净工资*/
    public static final Integer GRANT_SERVICE_TYPE_GRANT = 2;
    /** 发放服务标识：1-仅个税*/
    public static final Integer GRANT_SERVICE_TYPE_TAX = 1;
    /** 发放服务标识：0-工资和税*/
    public static final Integer GRANT_SERVICE_TYPE_GRANT_AND_TAX = 0;
    /** 国籍：中国*/
    public static final String COUNTRY_CODE_CHINA = "CN";
    /** 发放方式:1-中智上海账户*/
    public static final Integer GRANT_MODE_LOCAL = 1;
    /** 发放方式:2-中智代发（委托机构）*/
    public static final Integer GRANT_MODE_SUPPLIER = 2;
    /** 发放方式:3-客户自行*/
    public static final Integer GRANT_MODE_CUSTOMER = 3;
    /** 发放方式:4-中智代发（客户账户）*/
    public static final Integer GRANT_MODE_INDEPENDENCE = 4;
    /** 发放方式名称:1-中智上海账户*/
    public static final String GRANT_ACCOUNT_NAME_LOCAL = "中智上海账户";
    /** 发放方式名称:2-中智代发（委托机构）*/
    public static final String GRANT_ACCOUNT_NAME_SUPPLIER = "中智代发（委托机构）";
    /** 发放方式名称:3-客户自行*/
    public static final String GRANT_ACCOUNT_NAME_CUSTOMER = "客户自行";
    /** 发放方式名称:4-中智代发（客户账户）*/
    public static final String GRANT_ACCOUNT_NAME_INDEPENDENCE = "中智代发（客户账户）";
    /** 规则类型:0-固定金额*/
    public static final Integer SALARY_GRANT_RULE_TYPE_AMOUNT = 0;
    /** 规则类型:1-固定比例*/
    public static final Integer SALARY_GRANT_RULE_TYPE_RATIO = 1;
    /** 币种：CNY-人民币*/
    public static final String CURRENCY_CNY = "CNY";
    /** 币种：USD-美元*/
    public static final String CURRENCY_USD = "USD";
    /** 币种：EUR-欧元*/
    public static final String CURRENCY_EUR = "EUR";
    /** 汇率计算方式：0 - 固定*/
    public static final Integer EXCHANGE_CALC_MODE_FIXED_RATE = 0;
    /** 汇率计算方式：1 - 实时*/
    public static final Integer EXCHANGE_CALC_MODE_CURRENT_EXCHANGE_RATE = 1;
    /** 汇率计算方式：2 - 记账*/
    public static final Integer EXCHANGE_CALC_MODE_RECORDING_RATE = 2;
    /** 任务单类型:0 - 主表*/
    public static final Integer SALARY_GRANT_TASK_TYPE_MAIN_TASK = 0;
    /** 任务单类型:1-中智上海账户*/
    public static final Integer SALARY_GRANT_TASK_TYPE_LOCAL = 1;
    /** 任务单类型:2-中智代发（委托机构）*/
    public static final Integer SALARY_GRANT_TASK_TYPE_SUPPLIER = 2;
    /** 任务单类型:3-客户自行*/
    public static final Integer SALARY_GRANT_TASK_TYPE_CUSTOMER = 3;
    /** 任务单类型:4-中智代发（客户账户）*/
    public static final Integer SALARY_GRANT_TASK_TYPE_INDEPENDENCE = 4;
    /** 雇员变更类型:1 - 国籍*/
    public static final String EMPLOYEE_ALTER_TYPE_COUNTRY = "country";
    /** 雇员变更类型:2 - 发放方式*/
    public static final String EMPLOYEE_ALTER_TYPE_GRANT_MODE = "grantMode";
    /** 雇员变更类型:3 - 发放账户*/
    public static final String EMPLOYEE_ALTER_TYPE_GRANT_ACCOUNT = "grantAccount";
    /** 任务单备注:雇员信息发生变化*/
    public static final String TASK_REMARK = "雇员信息发生变化";
    /**  薪资发放时段:1-上午，2-下午*/
    public static final Integer GRANT_TIME_AM = 1;
    /**  变更操作:0-修改*/
    public static final Integer CHANGE_OPERATION_UPDATE = 0;
    /**  变更操作:1-删除*/
    public static final Integer CHANGE_OPERATION_DELETE = 1;
    /**  变更操作:2-新增*/
    public static final Integer CHANGE_OPERATION_INSERT = 2;
    /** 任务单类型:main - 主表*/
    public static final String SALARY_GRANT_TASK_TYPE_MAIN = "main";
    /** 任务单类型:sub - 子表*/
    public static final String SALARY_GRANT_TASK_TYPE_SUB = "sub";
    /** 发放状态:0-正常*/
    public static final Integer GRANT_STATUS_NORMAL = 0;
    /** 发放状态:1-手动暂缓*/
    public static final Integer GRANT_STATUS_MANUAL_REPRIEVE = 1;
    /** 发放状态:2-自动暂缓*/
    public static final Integer GRANT_STATUS_AUTO_REPRIEVE = 2;
    /** 发放状态:3-退票*/
    public static final Integer GRANT_STATUS_REFUND = 3;
    /** 发放类型:1-正常发放*/
    public static final Integer GRANT_TYPE_NORMAL = 1;
    /** 发放类型:2-调整发放*/
    public static final Integer GRANT_TYPE_ADJUST = 2;
    /** 发放类型:3-回溯发放*/
    public static final Integer GRANT_TYPE_BACK_TRACE = 3;
    /** 发放类型:4-暂缓再发放*/
    public static final Integer GRANT_TYPE_REPRIEVE = 4;
    /** 发放类型:5-退票发放*/
    public static final Integer GRANT_TYPE_REFUND = 5;
    /** 发放类型:6-现金*/
    public static final Integer GRANT_TYPE_CASH = 6;
    /** 合同我方-客服中心:AF*/
    public static final String CONTRACT_FIRST_PARTY_CMY_1 = "AF";
    /** 合同我方-客服中心:FC*/
    public static final String CONTRACT_FIRST_PARTY_CMY_2 = "FC";
    /** 合同我方-客服中心:BPO*/
    public static final String CONTRACT_FIRST_PARTY_CMY_3 = "BPO";
    /** 合同我方-结算中心:AF-1*/
    public static final String CONTRACT_FIRST_PARTY_STM_1 = "1";
    /** 合同我方-结算中心:FC-2*/
    public static final String CONTRACT_FIRST_PARTY_STM_2 = "2";
    /** 合同我方-结算中心:BPO-3*/
    public static final String CONTRACT_FIRST_PARTY_STM_3 = "3";
    /** 拆分条件:发放账户*/
    public static final String SPLIT_CONDITION = "grantAccountCode";
    /** 薪资发放主任务单编号前缀标识：SGT*/
    public static final String SALARY_GRANT_MAIN_TASK_ENTITY_PREFIX = "SGT";
    /** 薪资发放子任务单编号前缀标识：LTB - 中智上海发放（人民币）*/
    public static final String SALARY_GRANT_SUB_TASK_LOCAL_RMB_ENTITY_PREFIX = "LTB";
    /** 薪资发放子任务单编号前缀标识：LTW - 中智上海发放（外币）*/
    public static final String SALARY_GRANT_SUB_TASK_LOCAL_FOREIGN_CURRENCY_ENTITY_PREFIX = "LTW";
    /** 薪资发放子任务单编号前缀标识：STA - 中智代发（委托机构）*/
    public static final String SALARY_GRANT_SUB_TASK_NONLOCAL_ENTITY_PREFIX = "STA";
    /** 薪资发放子任务单编号前缀标识：LCT - 客户自行发放*/
    public static final String SALARY_GRANT_SUB_TASK_CUSTOMER_ENTITY_PREFIX = "LCT";
    /** 薪资发放子任务单编号前缀标识：LAT - 中智代发（客户账户）*/
    public static final String SALARY_GRANT_SUB_TASK_INDEPENDENCE_ENTITY_PREFIX = "LAT";

    /** 任务单状态：0-草稿*/
    public static final String TASK_STATUS_DRAFT = "0";
    /** 任务单状态：1-审批中*/
    public static final String TASK_STATUS_APPROVAL= "1";
    /** 任务单状态：2-审批通过*/
    public static final String TASK_STATUS_PASS = "2";
    /** 任务单状态：3-审批拒绝*/
    public static final String TASK_STATUS_REFUSE = "3";
    /** 任务单状态：4-失效*/
    public static final String TASK_STATUS_CANCEL= "4";
    /** 任务单状态：5-待生成*/
    public static final String TASK_STATUS_WAIT = "5";
    /** 任务单状态：6-未支付*/
    public static final String TASK_STATUS_UNPAID = "6";
    /** 任务单状态：7-已支付*/
    public static final String TASK_STATUS_PAYMENT= "7";
    /** 任务单状态：8-撤回*/
    public static final String TASK_STATUS_RETREAT= "8";
    /** 任务单状态：9-驳回*/
    public static final String TASK_STATUS_REJECT= "9";
    /** 任务单状态：10-待合并*/
    public static final String TASK_STATUS_COMBINE_WAIT= "10";
    /** 任务单状态：11-已合并*/
    public static final String TASK_STATUS_COMBINE= "11";
    /** 任务单状态：12-已确认*/
    public static final String TASK_STATUS_CONFIRM= "12";
    /** 任务单状态：13-作废*/
    public static final String TASK_STATUS_NULLIFY= "13";

    /** 任务单状态常量*/
    public static final String TASK_REFER= "refer";
    public static final String TASK_PEND= "pend";
    public static final String TASK_APPROVAL= "approval";
    public static final String TASK_ADOPT= "adopt";
    public static final String TASK_REFUSE= "refuse";
    public static final String TASK_CANCEL= "cancel";

    /** 系统 英文常量*/
    public static final String SYSTEM_EN= "system";

    /** 个税期间：0-当月*/
    public static final Integer TAX_PERIOD_CURRENT_MONTH = 0;
    /** 个税期间：1-下月*/
    public static final Integer TAX_PERIOD_NEXT_MONTH = 1;
    /** 个税期间：2-下下月*/
    public static final Integer TAX_PERIOD_NEXT_TWO_MONTH = 2;

    /** 结算中心业务类型*/
    public static final Integer SETTLEMENT_CENTER_BUSINESS_TYPE_SG = 5;
    /** 结算中心支付状态*/
    public static final Integer SETTLEMENT_CENTER_PAY_STATUS_SUCCESS = 9;

    /** 申报账户类别: 1-大库（FC目前服务协议只配置FC大库） */
    public static final int DECLARATION_ACCOUNT_CATEGORY_DEPOT_BIG = 1;
    /** 申报账户类别: 2-独立库 */
    public static final int DECLARATION_ACCOUNT_CATEGORY_DEPOT_INDEPENDENT = 2;

    /** 驳回信息*/
    public static final String REJECT_MESSAGE = "由结算中心驳回";

}

