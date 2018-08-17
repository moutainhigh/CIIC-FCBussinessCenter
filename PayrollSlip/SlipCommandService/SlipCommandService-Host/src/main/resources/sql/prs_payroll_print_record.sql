DROP TABLE IF EXISTS `prs_payroll_print_record`;

CREATE TABLE `prs_payroll_print_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '打印记录ID',
  `task_id` varchar(50) NOT NULL COMMENT '任务单编号',
  `task_title` varchar(50) DEFAULT NULL COMMENT '任务单标题',
  `management_id` varchar(9) DEFAULT NULL DEFAULT '' COMMENT '管理方ID',
  `management_name` varchar(30) DEFAULT NULL COMMENT '管理方名称',
  `batch_id` varchar(50) DEFAULT '' COMMENT '薪酬计算批次号',
  `period` varchar(20) DEFAULT '' DEFAULT '' COMMENT '薪资周期',
  `personnel_income_year_month` varchar(200) DEFAULT NULL COMMENT '工资年月',
  `print_file` varchar(100) DEFAULT NULL COMMENT '打印文件',
  `status` int(11) DEFAULT NULL COMMENT '状态  1：未打印  2：已打印',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `is_active` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否可用',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `created_by` varchar(20) NOT NULL COMMENT '创建者登录名',
  `modified_by` varchar(20) NOT NULL COMMENT '修改者登录名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工资单任务单打印记录';
