package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 薪资发放任务单子表 Mapper 接口
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
@Component
public interface SalaryGrantSubTaskMapper extends BaseMapper<SalaryGrantSubTaskPO> {
    /**
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 待审批:1-审批中 角色=审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:1-审批中 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:审批通过 2-审批通过、10-待合并、11-已合并 角色=操作员、审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:审批拒绝 3-审批拒绝、8-撤回 角色=操作员、审核员（查历史表）
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放报盘任务单列表
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);
}