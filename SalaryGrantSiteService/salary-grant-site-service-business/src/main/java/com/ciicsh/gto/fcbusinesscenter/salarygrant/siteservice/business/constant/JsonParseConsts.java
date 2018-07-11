package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant;

public class JsonParseConsts {
    /** 雇员服务协议*/
    /** 雇员中智终身编号*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_EMLOYEE_ID = "empId";
    /** 公司名称*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_COMPANY_NAME = "companyName";
    /** 公司编号*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_COMPANY_ID = "companyCode";
    /** 雇佣性质（当前公司）：1:派遣 2:代理 3:外包*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_EMPLOYED_TYPE = "employedType";
    /** 服务周期规则Id*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_CYCLE_RULE_ID = "cycleRuleId";
    /** 薪资发放信息*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SALARY_GRANT_INFO = "salaryGrantInfo";
    /** 发放方式*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_GRANT_TYPE = "grantType";
    /** 旧值--发放服务标识：0-薪资发放，1-个税，2-薪资发放 + 个税 grantServiceType
     *  新值--服务类别 ( 0 - 工资和税, 1 - 仅个税, 2 - 净工资, 3 - 仅计算 ) serviceCategory
     * */
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_GRANT_SERVICE_TYPE = "serviceCategory";
    /** 薪资发放规则Id*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_EMP_SALARY_GRANT_RULE_ID = "grantRuleId";
    /** 汇率计算方式（0 - 固定， 1 - 实时，2-记账）*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_EXCHANGE_CALC_MODE = "exechangeRateSettlementType";
    /** 币种（CNY-人民币，USD-美元，EUR-欧元）*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_CURRENCY_TYPE = "currencyType";
    /** 汇率*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_EXCHANGE_RATE = "exchangeRate";
    /** 是否包括社保和公积金*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_IS_WELFARE_INCLUDED = "isWelfareIncluded";
    /** 委托合同流水号*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_COMMISSION_CONTRACT_SERIAL_NUMBER = "commissionContractSerialNumber";
    /** 社保数据来源 ( 0 - AF, 1 - 客户 )*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SOCIAL_SECURITY_SOURCE = "socialSecuritySource";
    /** 个人养老 ( 0 - 工资内含, 1 - 工资不含 ) */
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_PERSONAL_PENSION = "personalPension";
    /** 个人医疗 ( 0 - 工资内含, 1 - 工资不含 ) */
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_PERSONAL_MEDICAL_TREATMENT = "personalMedicalTreatment";
    /** 个人失业( 0 - 工资内含, 1 - 工资不含 )*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_INDIVIDUAL_UNEMPLOYEMENT = "individualUnemployment";
    /** 个人公积金( 0 - 工资内含, 1 - 工资不含 )*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_INDIVIDUAL_PROVIDENT_FUND = "individualProvidentFund";
    /** 供应商信息*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_DETAIL = "supplierDetail";
    /** 供应商收款账户*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE = "account";
    /** 供应商收款账户名称*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE_NAME = "accountName";
    /** 付款账号*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_ACCOUNT = "paymentBankAccount";
    /** 付款账户名*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_ACCOUNT_NAME = "paymentBankAccountName";
    /** 付款银行名*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_NAME = "paymentBankName";
    /** 账单结算信息*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_BILL_INFO = "billingInfo";
    /** 业务合同编号*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_CONTRACTID = "contractId";
    /** 合同类型 (0-渠道方合同 1-业务合同 2-内部确认单（FC和AF签订）3-合同附件 4-委托合同)*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_TYPE = "contractType";
    /** 合同我方（AF/FC/BPO）*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_FIRST_PARTY = "contractFirstParty";
    /** 个税信息*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_INFO = "taxInfo";
    /** 个税期间（0-当月 1-下月 2-下下月）*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_PERIOD = "taxPeriod";
    /** 申报账户详细信息*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_DECLARATION_ACCOUNT_DETAIL = "declarationAccountDetail";
    /** 缴纳账户详细信息*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRIBUTION_ACCOUNT_DETAIL = "contributionAccountDetail";
    /** 个税账户*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT = "account";
    /** 个税账户类别 (1-大库（FC目前服务协议只配置FC大库），2-独立库)*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT_CATEGORY = "source";

    /** 扣社保小计*/
    public static final String EMLOYEE_RESULT_ITMES_SOCIAL_SECURITY = "扣社保小计";
    /** 扣公积金小计*/
    public static final String EMLOYEE_RESULT_ITMES_PROVIDENT_FUND = "扣公积金小计";
    /** 年度奖金*/
    public static final String EMLOYEE_RESULT_ITMES_YEAR_BONUS = "年度奖金";
    /** 实际工作年限数*/
    public static final String EMLOYEE_RESULT_ITMES_LEAVING_YEARS = "实际工作年限数";

    /** 雇员变更日志:国籍*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD1 = "country_code";
    /** 雇员变更日志：发放账户*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD2 = "grant_account_code";
    /** 雇员变更日志：发放方式*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD3 = "grant_mode";
    /** 雇员变更日志：银行卡号*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD4 = "card_num";
    /** 雇员变更日志：开户名*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD5 = "account_name";
    /** 雇员变更日志：收款行行号*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD6 = "bank_code";
    /** 雇员变更日志：收款行名称*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD7 = "deposit_bank";
    /** 雇员变更日志：发放币种*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD8 = "currency_code";
    /** 雇员变更日志：汇率*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD9 = "exchange";
    /** 雇员变更日志：规则金额*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD10 = "rule_amount";
    /** 雇员变更日志：规则比例*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD11 = "rule_ratio";
    /** 雇员变更日志：薪资发放日期*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD12 = "grant_date";
    /** 雇员变更日志：薪资发放时段*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD13 = "grant_time";
    /** 雇员变更日志:原值*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD14 = "old_value";
    /** 雇员变更日志:新值*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD15 = "new_value";
    /** 雇员变更日志:原时间*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD16 = "old_date";
    /** 雇员变更日志:修改时间*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD17 = "new_date";
    /** 雇员变更日志:变更类型*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD18 = "type";
    /** 雇员变更日志:变更操作*/
    public static final String EMLOYEE_CHANGE_LOG_FIELD19 = "operation";

    public static final String EMPLOYEE_CODE_CN = "雇员编号";

    public static final String EMPLOYEE_NAME_CN = "雇员名称";

    public static final String EMPLOYEE_COUNTRY_CODE_CN = "国家代码";

    public static final String EMPLOYEE_SERVICE_AGREE = "雇员服务协议";

    public static final String ACTUAL_PAY = "应发工资";

    public static final String EMPLOYEE_NET_PAY = "实发工资";

    public static final String TAX_TOTAL = "个税合计";



}
