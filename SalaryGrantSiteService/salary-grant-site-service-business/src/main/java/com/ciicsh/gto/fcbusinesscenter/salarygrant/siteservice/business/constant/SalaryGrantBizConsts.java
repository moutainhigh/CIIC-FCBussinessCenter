package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant;

public class SalaryGrantBizConsts {
    /** 发放服务标识：0-薪资发放*/
    public static final Integer GRANT_SERVICE_TYPE_GRANT = 0;
    /** 发放服务标识：2-薪资发放 + 个税*/
    public static final Integer GRANT_SERVICE_TYPE_GRANT_AND_TAX = 2;
    /** 国籍：中国*/
    public static final String COUNTRY_CODE_CHINA = "CN";
    /** 发放方式:1-中智上海账户*/
    public static final Integer GRANT_MODE_LOCAL = 1;
    /** 发放方式:2-中智代发（委托机构）*/
    public static final Integer GRANT_MODE_SUPPLIER = 2;
    /** 发放方式:3-中智代发（客户账户）*/
    public static final Integer GRANT_MODE_INDEPENDENCE = 3;
    /** 发放方式:4-客户自行*/
    public static final Integer GRANT_MODE_CUSTOMER = 4;
    /** 规则类型:0-固定金额*/
    public static final Integer SALARY_GRANT_RULE_TYPE_AMOUNT = 0;
    /** 规则类型:1-固定比例*/
    public static final Integer SALARY_GRANT_RULE_TYPE_RATIO = 1;
    /** 币种：CNY-人民币*/
    public static final String CURRENCY_CNY = "CNY";
    /** 汇率计算方式：0 - 固定*/
    public static final Integer EXCHANGE_CALC_MODE_FIX = 0;
    /** 汇率计算方式：1 - 实时*/
    public static final Integer EXCHANGE_CALC_MODE_REAL_TIME = 1;
    /** 任务单状态：0 - 草稿 */
    public static final String SALARY_GRANT_TASK_STAUS_DRAFT = "0";
    /** 任务单状态：2 - 审批通过 */
    public static final String SALARY_GRANT_TASK_STAUS_APPROVE = "2";
    /** 任务单类型:0 - 主表*/
    public static final Integer SALARY_GRANT_TASK_TYPE_MAIN_TASK = 0;
    /** 任务单类型:1-中智上海账户*/
    public static final Integer SALARY_GRANT_TASK_TYPE_LOCAL = 1;
    /** 任务单类型:2-中智代发（委托机构）*/
    public static final Integer SALARY_GRANT_TASK_TYPE_SUPPLIER = 2;
    /** 任务单类型:3-中智代发（客户账户）*/
    public static final Integer SALARY_GRANT_TASK_TYPE_INDEPENDENCE = 3;
    /** 任务单类型:4-客户自行*/
    public static final Integer SALARY_GRANT_TASK_TYPE_CUSTOMER = 4;
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
    /** 发放状态:2-自动暂缓*/
    public static final Integer GRANT_STATUS_AUTO_REPRIEVE = 2;
    /** 发放状态:3-退票*/
    public static final Integer GRANT_STATUS_REFUND = 3;
    /** 发放类型:2-调整发放*/
    public static final Integer GRANT_TYPE_ADJUST = 2;
    /** 发放类型:3-回溯发放*/
    public static final Integer GRANT_TYPE_BACK_TRACE = 3;
    /** 发放类型:5-退票发放*/
    public static final Integer GRANT_TYPE_REFUND = 5;
    /** 合同我方-客服中心:AF*/
    public static final String CONTRACT_FIRST_PARTY_CMY_1 = "AF";
    /** 合同我方-客服中心:FC*/
    public static final String CONTRACT_FIRST_PARTY_CMY_2 = "FC";
    /** 合同我方-结算中心:AF-1*/
    public static final String CONTRACT_FIRST_PARTY_STM_1 = "1";
    /** 合同我方-结算中心:FC-2*/
    public static final String CONTRACT_FIRST_PARTY_STM_2 = "2";
    /** 合同我方-结算中心:BPO-3*/
    public static final String CONTRACT_FIRST_PARTY_STM_3 = "3";
    /** 拆分条件:发放账户*/
    public static final String SPLIT_CONDITION = "grantAccountCode";
    /** 上海本地发放任务单编号前缀标识：人民币发放-LTB*/
    public static final String GRANT_MODE_LOCAL_LTB = "LTB";
    /** 上海本地发放任务单编号前缀标识：外币发放-LTW*/
    public static final String GRANT_MODE_LOCAL_LTW = "LTW";

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
    /** 任务单状态：5-待支付*/
    public static final String TASK_STATUS_WAIT = "5";
    /** 任务单状态：6-未支付*/
    public static final String TASK_STATUS_UNPAID = "6";
    /** 任务单状态：7-已支付*/
    public static final String TASK_STATUS_PAYMENT= "7";
    /** 任务单状态：8-驳回*/
    public static final String TASK_STATUS_REJECT= "8";

}

