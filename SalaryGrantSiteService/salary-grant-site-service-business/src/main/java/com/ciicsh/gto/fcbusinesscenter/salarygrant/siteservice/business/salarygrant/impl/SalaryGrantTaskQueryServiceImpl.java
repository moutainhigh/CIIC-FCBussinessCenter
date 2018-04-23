package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 薪资发放任务单查询 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
@Service
public class SalaryGrantTaskQueryServiceImpl implements SalaryGrantTaskQueryService {
    @Autowired
    SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;

    /**
     * @description 待提交任务单：0-草稿 角色=操作员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> queryTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.submitList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @description 待审批任务单:1-审批中 角色=审核员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> queryTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.approveList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @description 已处理任务单:1-审批中 角色=操作员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> queryTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.haveApprovedList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @description 已处理任务单:审批通过 2-审批通过、6-未支付、7-已支付 角色=操作员、审核员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> queryTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.passList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @description 已处理任务单:审批拒绝 3-审批拒绝、8-撤回、9-驳回 角色=操作员、审核员（查历史表）
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> queryTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.rejectList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @description 已处理任务单:已处理:4-失效 角色=操作员、审核员（查历史表）
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> queryTaskForInvalidPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.invalidList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @description 根据主表任务单编号查询
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> querySubTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
//        List<SalaryGrantTaskBO> list = salaryGrantSubTaskMapper.subTaskList(page, salaryGrantTaskBO);
//        page.setRecords(list);
//        return page;
        return null;
    }

}
