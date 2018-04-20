package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;

/**
 * <p>
 * 薪资发放供应商任务单 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
public interface SalaryGrantSupplierSubTaskService extends IService<SalaryGrantSubTaskPO> {

    /**
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 待审批:1-审批中 角色=审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> querySupplierSubTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:1-审批中 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> querySupplierSubTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:审批通过 2-审批通过、10-待合并、11-已合并 角色=操作员、审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> querySupplierSubTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:审批拒绝 3-审批拒绝、8-撤回 角色=操作员、审核员（查历史表）
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> querySupplierSubTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);
}
