 /**
 * @author baofeng@ciicsh.
 * @createTime 2018-10-25 17:06:43
 * @description 薪资账套下的薪资项信息表
 */

 /* 添加列is_show,薪资项是否显示 */;
ALTER TABLE `gtopayrolldb`.`pr_payroll_account_item_relation`
  ADD COLUMN `is_show` BIT(1) DEFAULT b'1' NOT NULL COMMENT '是否显示' AFTER `payroll_item_alias`;
