package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowTaskInfoBO;
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
    @Autowired
    SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    WorkFlowTaskInfoMapper workFlowTaskInfoMapper;

    /**
     * 查询薪资发放任务单列表
     * @param bo
     * @return Page<SalaryGrantTaskBO>
     */
    @Override
    public Page<SalaryGrantTaskBO> salaryGrantList(SalaryGrantTaskBO bo) {
        Page<SalaryGrantTaskBO> page = null;
        Page<SalaryGrantTaskBO> paging = new Page<SalaryGrantTaskBO>(bo.getCurrent(), bo.getSize());
        if (SalaryGrantBizConsts.TASK_REFER.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForSubmitPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_PEND.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForApprovePage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_APPROVAL.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForHaveApprovedPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_ADOPT.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForPassPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_REFUSE.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForRejectPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_CANCEL.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForInvalidPage(paging, bo);
        }
        return page;
    }

    /**
     * @description 根据任务单编号查询任务单
     * @author chenpb
     * @since 2018-04-25
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public SalaryGrantTaskBO selectTaskByTaskCode (SalaryGrantTaskBO salaryGrantTaskBO) {
        SalaryGrantTaskBO bo;
        if (SalaryGrantBizConsts.TASK_STATUS_REFUSE.equals(salaryGrantTaskBO.getTaskStatus()) || SalaryGrantBizConsts.TASK_STATUS_CANCEL.equals(salaryGrantTaskBO.getTaskStatus())) {
            bo = salaryGrantTaskHistoryMapper.selectTaskByTaskCode(salaryGrantTaskBO);
        } else if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType())) {
            bo = salaryGrantMainTaskMapper.selectTaskByTaskCode(salaryGrantTaskBO);
        } else {
            bo = salaryGrantSubTaskMapper.selectTaskByTaskCode(salaryGrantTaskBO);
        }
        return bo;
    }

    /**
     * 根据任务单编号查询操作记录
     * @param bo
     * @return
     */
    @Override
    public Page<WorkFlowTaskInfoBO> operation(SalaryGrantTaskBO bo) {
        Page<WorkFlowTaskInfoBO> page = new Page<WorkFlowTaskInfoBO>(bo.getCurrent(), bo.getSize());
        List<WorkFlowTaskInfoBO> list = workFlowTaskInfoMapper.operation(page, bo);
        return page.setRecords(list);
    }

    /**
     * @description 待提交任务单：0-草稿 角色=操作员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> queryTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
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
    private Page<SalaryGrantTaskBO> queryTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
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
    private Page<SalaryGrantTaskBO> queryTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
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
    private Page<SalaryGrantTaskBO> queryTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
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
    private Page<SalaryGrantTaskBO> queryTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantTaskHistoryMapper.rejectList(page, salaryGrantTaskBO);
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
    private Page<SalaryGrantTaskBO> queryTaskForInvalidPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantTaskHistoryMapper.invalidList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @description 根据主表任务单编号查询字表任务单
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> querySubTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantSubTaskMapper.subTaskList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

}
