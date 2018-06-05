DROP TABLE IF EXISTS `prs_payroll`;

CREATE TABLE `prs_payroll` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工资单ID',
  `payroll_code` varchar(20) DEFAULT NULL COMMENT '工资单编号',
  `main_task_id` varchar(20) DEFAULT NULL COMMENT '任务单id',
  `management_id` varchar(15) NOT NULL DEFAULT '' COMMENT '管理方ID',
  `management_name` varchar(30) DEFAULT NULL,
  `employee_id` varchar(9) NOT NULL DEFAULT '' COMMENT '雇员ID',
  `employee_name` varchar(30) NOT NULL DEFAULT '' COMMENT '雇员ID',
  `net_pay` varchar(50) DEFAULT NULL COMMENT '实发工资',
  `period` varchar(20) DEFAULT NULL COMMENT '薪资周期',
  `personnel_income_year_month` varchar(200) DEFAULT NULL COMMENT '工资年月',
  `batch_id` varchar(50) NOT NULL DEFAULT '' COMMENT '工资单批次',
  `template_id` bigint(20) DEFAULT NULL COMMENT '模板id',
  `template_name` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `channel` int(11) DEFAULT '2' COMMENT '渠道：1. 外部  2：薪酬计算  3：合并',
  `items` json NOT NULL COMMENT '薪资项',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `is_active` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否可用',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `created_by` varchar(50) NOT NULL COMMENT '创建者登录名',
  `modified_by` varchar(50) NOT NULL COMMENT '修改者登录名',
  PRIMARY KEY (`id`),
  KEY `IX_PayRoll_ManagementID` (`management_id`) USING BTREE,
  KEY `IX_PayRoll_EmployeeID` (`employee_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='已发布的工资单';