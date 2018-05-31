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
    /** 发放服务标识：0-薪资发放，1-个税，2-薪资发放 + 个税*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_GRANT_SERVICE_TYPE = "grantServiceType";
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
    /** 供应商名称*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_NAME = "supplierName";
    /** 供应商收款账户*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE = "supplierAccountReceivale";
    /** 供应商收款账户名称*/
    public static final String EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE_NAME = "supplierAccountReceivaleName";
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

}
