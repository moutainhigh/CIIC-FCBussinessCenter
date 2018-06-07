DROP TABLE IF EXISTS `prs_payroll_express`;

CREATE TABLE `prs_payroll_express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_id` varchar(20) NOT NULL COMMENT '任务单编号',
  `recipient` varchar(200) DEFAULT NULL COMMENT '收件人',
  `receive_address` varchar(200) DEFAULT NULL COMMENT '收件人地址',
  `postcode` varchar(200) DEFAULT NULL COMMENT '邮编',
  `recipient_type` int(11) DEFAULT NULL COMMENT '收件人类型  1：HR  2：雇员  3：其他',
  `express_company` varchar(20) DEFAULT NULL COMMENT '快递公司',
  `express_number` varchar(20) DEFAULT NULL COMMENT '快递单号',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `is_active` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否可用',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `created_by` varchar(20) NOT NULL COMMENT '创建者登录名',
  `modified_by` varchar(20) NOT NULL COMMENT '修改者登录名',
  PRIMARY KEY (`id`),
  KEY `IX_PaperPayrollRecord_TaskId` (`task_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工资单邮寄快递信息';
