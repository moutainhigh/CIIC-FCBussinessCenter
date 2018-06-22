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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 流程定义
     */
    enum ProcessDefinitionKey {
        MAIN_TASK("payroll_main", "主任务单"),
        SUB_TASK("payroll_sub", "子任务单");

        private String key;
        private String name;

        ProcessDefinitionKey(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }
    }
}

