package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant;

/**
 * 薪资发放工作流
 * 枚举常量
 *
 * @author chenpb
 */
public interface SalaryGrantWorkFlowEnums {
    /**
     * 操作类型
     */
    enum ActionType {
        ACTION_SUBMIT("submit", "提交"),
        ACTION_REJECT("reject", "审批拒绝"),
        ACTION_APPROVAL("approval", "审批通过");

        private String action;
        private String name;

        ActionType(String action, String name) {
            this.action = action;
            this.name = name;
        }

        public String getAction() {
            return action;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 流程定义
     * SGT : payroll_main (薪资发放任务单SGT)
     * LTB : payroll_local_domestic_currency (薪资发放本地本币任务单LTB)
     * LTW : payroll_local_foreign_currency (薪资发放本地外币任务单LTW)
     * STA : payroll_nonlocal (薪资发放外地任务单STA)
     * SPT : supplier_payment (供应商支付任务单SPT)
     */
    enum ProcessDefinitionKey {
        SGT("SGT", "payroll_main"),
        LTB("LTB", "payroll_local_domestic_currency"),
        LTW("LTW", "payroll_local_foreign_currency"),
        STA("STA", "payroll_nonlocal"),
        SPT("SPT", "supplier_payment");

        private String type;
        private String key;

        ProcessDefinitionKey(String type, String key) {
            this.type = type;
            this.key = key;
        }

        public String getType() {
            return type;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * 任务单业务处理结果
     */
    enum TaskResult {
        SUB_TASK(0, "子表任务，不进行处理"),
        ADVANCED (1, "未来款，无垫付流程。前端代码获取该信息，需要显示提示框是否走代垫流程，如果确认走代垫流程，需要调用客服中心代垫流程页面链接。"),
        OVERDUE(2, "未来款，垫付流程已逾期。提示业务操作人员，垫付已逾期，不能提交审批 --> 未来款，垫付已逾期"),
        HANDLE(3, "已来款或已垫付（可以提交审批）"),
        ERROR(99, "错误");

        private Integer result;
        private String extension;

        TaskResult(Integer result, String extension) {
            this.result = result;
            this.extension = extension;
        }

        public Integer getResult() {
            return result;
        }

        public String getExtension() {
            return extension;
        }
    }

    /**
     * 角色枚举
     */
    enum Role {
        OPERATOR("XTJ0171", "payroll_operator"),
        AUDIT ("XTJ0170", "payroll_audit");

        private String id;
        private String name;

        Role(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}

