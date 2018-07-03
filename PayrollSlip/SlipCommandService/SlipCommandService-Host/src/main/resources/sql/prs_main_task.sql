DROP TABLE IF EXISTS `prs_main_task`;

CREATE TABLE `prs_main_task` (
  `main_task_id` varchar(50) NOT NULL COMMENT '任务单编号',
  `title` varchar(50) DEFAULT NULL COMMENT '任务单名称',
  `management_id` varchar(15) NOT NULL COMMENT '管理方编号',
  `management_name` varchar(30) DEFAULT NULL COMMENT '管理方名称',
  `batch_id` varchar(50) DEFAULT '' COMMENT '薪酬计算批次号',
  `period` varchar(20) DEFAULT '' COMMENT '薪资周期',
  `personnel_income_year_month` varchar(200) DEFAULT NULL COMMENT '工资年月',
  `employees` json DEFAULT NULL COMMENT '雇员列表',
  `total_count` int(11) DEFAULT '0' COMMENT '总人数',
  `chinese_count` int(11) DEFAULT '0' COMMENT '中方发薪人数',
  `foreigner_count` int(11) DEFAULT '0' COMMENT '外方发薪人数',
  `publish_date` datetime DEFAULT NULL COMMENT '邮件发送日期',
  `publish_exec_date` datetime DEFAULT NULL COMMENT '邮件发送执行日期',
  `publish_manual_date` datetime DEFAULT NULL COMMENT '主动发送日期',
  `upload_date` datetime DEFAULT NULL COMMENT '上传智翔通日期',
  `upload_exec_date` datetime DEFAULT NULL COMMENT '上传智翔通执行日期',
  `publish_manual_remark` varchar(500) DEFAULT NULL COMMENT '主动发送备注',
  `publish_state` int(11) DEFAULT '0' COMMENT '邮件发送状态 0: 未发送 1: 待发送 2: 发送中 3: 发送成功 4: 发送失败 5: 已忽略 6: 无关',
  `publish_fail_log` varchar(500) DEFAULT NULL COMMENT '邮件发送失败日志',
  `upload_state` int(11) DEFAULT '0' COMMENT '上传状态 0: 未上传 1: 待上传 2: 上传中 3: 上传成功 4: 上传失败 5: 已忽略 6: 无关',
  `payroll_type` int(11) DEFAULT '0' COMMENT '工资单类型:0-通用，1-纸质，2-邮件，3-网上查看',
  `selected_payroll_type` varchar(200) DEFAULT NULL COMMENT '选择的工资单类型，逗号分隔',
  `status` int(11) DEFAULT '0' COMMENT '状态:0-草稿，1-审批中，2-审批通过，3-审批拒绝，4-失效',
  `has_paper` tinyint(1) DEFAULT '0' COMMENT '是否含纸质',
  `comments` varchar(100) DEFAULT NULL COMMENT '修改实际发布日期原因',
  `template_id` bigint(20) DEFAULT NULL COMMENT '工资单模板id',
  `template_name` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `approver` varchar(200) DEFAULT NULL COMMENT '审批人',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '审批备注',
  `is_active` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否有效:1-有效，0-无效',
  `created_by` varchar(20) NOT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(20) NOT NULL COMMENT '最后修改人',
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`main_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工资单任务单主表';